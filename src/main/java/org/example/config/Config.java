package org.example.config;

import java.util.Set;

public final class Config {
    private Config() {}

    private static final Set<Integer> ALLOWED_X = Set.of(-4, -3, -2, -1, 0, 1, 2, 3, 4);
    private static final double MIN_Y = -5.0;
    private static final double MAX_Y = 5.0;
    private static final double MIN_R = 0.1;
    private static final double MAX_R = 3.0;

    private static final String KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String KAFKA_TOPIC = "results-topic";
    private static final String KAFKA_CONSUMER_GROUP = "statistics-group";

    public static Set<Integer> getAllowedX() { return ALLOWED_X; }
    public static double getMinY() { return MIN_Y; }
    public static double getMaxY() { return MAX_Y; }
    public static double getMinR() { return MIN_R; }
    public static double getMaxR() { return MAX_R; }

    public static String getKafkaBootstrapServers() { return KAFKA_BOOTSTRAP_SERVERS; }
    public static String getKafkaTopic() { return KAFKA_TOPIC; }
    public static String getKafkaConsumerGroup() { return KAFKA_CONSUMER_GROUP; }
}