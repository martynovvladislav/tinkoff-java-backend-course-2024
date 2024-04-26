package edu.java.bot.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaErrorHandler implements CommonErrorHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public boolean handleOne(
        @NotNull Exception exception,
        ConsumerRecord<?, ?> consumerRecord,
        @NotNull Consumer<?, ?> consumer,
        @NotNull MessageListenerContainer container
    ) {
        handle(exception, consumer);
        kafkaTemplate.send("updates_dlq", consumerRecord.value());
        return true;
    }

    @Override
    public void handleOtherException(
        @NotNull Exception exception,
        @NotNull Consumer<?, ?> consumer,
        @NotNull MessageListenerContainer container,
        boolean batchListener
    ) {
        handle(exception, consumer);
    }

    private void handle(Exception exception, Consumer<?, ?> consumer) {
        log.error("Exception during the message receiving: " + exception.getMessage());
    }
}