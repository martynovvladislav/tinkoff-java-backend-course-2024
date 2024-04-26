package edu.java.scrapper.kafkaTests;

import edu.java.scrapper.configuration.KafkaConfiguration;
import edu.java.scrapper.configuration.KafkaProducerProperties;
import edu.java.scrapper.dtos.LinkUpdateDto;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

@SpringBootTest(classes = {KafkaProducerTest.class, KafkaConfiguration.class})
@EnableConfigurationProperties(value = {KafkaProducerProperties.class})
public class KafkaProducerTest extends KafkaTest {

    @Autowired
    private KafkaProducerProperties kafkaProducerProperties;
    protected Map<String, Object> getConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bot");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "edu.java.scrapper.dtos.LinkUpdateDto");
        return props;
    }

    protected Map<String, Object> getProducerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerProperties.clientId());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperties.acksMode());
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
            (int) kafkaProducerProperties.deliveryTimeout().toMillis());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperties.lingerMs());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerProperties.batchSize());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Test
    void sendMessageTest() {
        KafkaTemplate<String, LinkUpdateDto> kafkaTemplate = new KafkaTemplate<>(
                new DefaultKafkaProducerFactory<>(getProducerProps())
        );

        try(KafkaConsumer<String, LinkUpdateDto> kafkaConsumer = new KafkaConsumer<>(getConsumerProps())) {
            kafkaConsumer.subscribe(List.of("updates_main"));
            kafkaTemplate.send(
                "updates_main",
                new LinkUpdateDto(URI.create("test"), "test", new ArrayList<>())
            );

            Iterable<ConsumerRecord<String, LinkUpdateDto>> consumerRecords = kafkaConsumer
                .poll(Duration.ofSeconds(3))
                .records("updates_main");

            List<ConsumerRecord<String, LinkUpdateDto>> result = new ArrayList<>();
            consumerRecords.forEach(result::add);
            Assertions.assertFalse(result.isEmpty());
        }
    }
}
