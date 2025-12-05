package org.example.kafka;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.config.Config;

import java.util.Properties;

@ApplicationScoped
public class KafkaProducerConfig {
    private KafkaProducer<String, String> producer;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.getKafkaBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        producer = new KafkaProducer<>(props);
        System.out.println("KafkaProducerConfig: Initialized");
    }

    @PreDestroy
    public void destroy() {
        if (producer != null) {
            producer.close();
            System.out.println("KafkaProducerConfig: Destroyed");
        }
    }

    public KafkaProducer<String, String> getProducer() {return producer;}
}