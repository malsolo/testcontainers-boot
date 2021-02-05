package com.malsolo.testcontainers.boot.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaBootProducer {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<Integer, String> kafkaTemplate;

    public void sendMessage(Integer key, String message) {
        var topic = kafkaProperties.getProducer().getTopic();
        log.info("About to send a message {} with key {} to topic {}", message, key, topic);
        var future = kafkaTemplate.send(topic, key, message);
        future.addCallback(new KafkaSendCallback<>() {
            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                log.info("Successfully sent message {} with key {} to topic {}", message, key, topic);
            }

            @Override
            public void onFailure(KafkaProducerException ex) {
                log.error("Error sending message {} with key {} to topic {}: {}", message, key, topic, ex.getMessage());
            }
        });
    }

}