package edu.java.scrapper.configuration;

import edu.java.scrapper.dtos.LinkUpdateDto;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic updatesMain() {
        return new NewTopic("updates_main", 1, (short) 1);
    }

    @Bean
    public NewTopic updatesDlq() {
        return new NewTopic("updates_dlq", 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, LinkUpdateDto> producerFactory(KafkaProducerProperties kafkaProducerProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.bootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerProperties.clientId());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperties.acksMode());
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,
            (int) kafkaProducerProperties.deliveryTimeout().toMillis());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperties.lingerMs());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerProperties.batchSize());
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, LinkUpdateDto> kafkaTemplate(ProducerFactory<String, LinkUpdateDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
