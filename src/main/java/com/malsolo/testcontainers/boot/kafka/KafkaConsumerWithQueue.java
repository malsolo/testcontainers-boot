package com.malsolo.testcontainers.boot.kafka;

import java.util.concurrent.BlockingQueue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
@Getter
public class KafkaConsumerWithQueue {

    private final BlockingQueue<String> queue;

    @KafkaListener(id = "KafkaBootConsumer",
        autoStartup = "${kafka.consumer.auto-start}",
        topics = "${kafka.consumer.topic}",
        groupId = "${kafka.consumer.group-id}",
        clientIdPrefix = "KafkaConsumerWithQueue-Prefix"
    )
    public void consume(@Payload String message,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
        @Header(KafkaHeaders.OFFSET) int offset,
        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key) {
        log.info("Received message {} from topic {}, partition {}, with offset {} and key {}",
            message, topic, partition, offset, key);
        queue.add(message);
    }
}
