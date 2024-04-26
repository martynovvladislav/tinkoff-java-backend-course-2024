package edu.java.scrapper.kafkaTests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class KafkaTest {
    public static KafkaContainer kafkaContainer;

    static {
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
    }

    @BeforeAll
    static void start() {
        kafkaContainer.start();
    }

    @AfterAll
    static void stop() {
        kafkaContainer.stop();
    }

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }
}
