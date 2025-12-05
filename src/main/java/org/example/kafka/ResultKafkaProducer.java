package org.example.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.config.Config;
import org.example.entities.ResultEntity;
import org.example.service.ResultEventPublisher;

@ApplicationScoped
public class ResultKafkaProducer implements ResultEventPublisher {
    private final ObjectMapper objectMapper;

    @Inject
    private KafkaProducerConfig producerConfig;

    public ResultKafkaProducer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void publish(ResultEntity entity) {
        try {
            String json = objectMapper.writeValueAsString(entity);
            String key = entity.getId() != null ? entity.getId().toString() : "unknown";

            ProducerRecord<String, String> record = new ProducerRecord<>(Config.getKafkaTopic(), key, json);

            producerConfig.getProducer().send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Kafka send error: " + exception.getMessage());
                } else {
                    System.out.println("Kafka: Sent to " + metadata.topic() +
                            ", partition=" + metadata.partition() +
                            ", offset=" + metadata.offset());
                }
            });

            producerConfig.getProducer().flush();
        } catch (Exception e) {
            System.err.println("Kafka serialization error: " + e.getMessage());
        }
    }
}