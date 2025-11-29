package org.example.kafka;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.entities.ResultEntity;
import org.example.service.StatisticsService;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class ResultKafkaConsumer {

    private static final String TOPIC = "results-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    private final ObjectMapper objectMapper;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private KafkaConsumer<String, String> consumer;
    private ExecutorService executor;

    @Inject
    private StatisticsService statisticsService;

    public ResultKafkaConsumer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public void onAppStart(@Observes @Initialized(ApplicationScoped.class) Object init) {
        startConsumer();
    }

    @PostConstruct
    public void init() {
        System.out.println("ResultKafkaConsumer: PostConstruct");
    }

    private synchronized void startConsumer() {
        if (initialized.getAndSet(true)) {
            return;
        }

        try {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "statistics-group-" + System.currentTimeMillis());
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(TOPIC));

            running.set(true);
            executor = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "kafka-consumer");
                t.setDaemon(true);
                return t;
            });
            executor.submit(this::consumeLoop);

            System.out.println("ResultKafkaConsumer: Started");
        } catch (Exception e) {
            System.err.println("ResultKafkaConsumer: Failed to start - " + e.getMessage());
        }
    }

    private void consumeLoop() {
        try {
            while (running.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {
                    processRecord(record);
                }
            }
        } catch (Exception e) {
            if (running.get()) {
                System.err.println("ResultKafkaConsumer: Error - " + e.getMessage());
            }
        } finally {
            closeConsumer();
        }
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        try {
            ResultEntity entity = objectMapper.readValue(record.value(), ResultEntity.class);
            statisticsService.processResult(entity);
        } catch (Exception e) {
            System.err.println("ResultKafkaConsumer: Parse error - " + e.getMessage());
        }
    }

    private void closeConsumer() {
        if (consumer != null) {
            try {
                consumer.close(Duration.ofSeconds(2));
            } catch (Exception ignored) {}
        }
    }

    @PreDestroy
    public void destroy() {
        running.set(false);
        if (executor != null) {
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
        System.out.println("ResultKafkaConsumer: Destroyed");
    }
}