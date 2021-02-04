package com.malsolo.testcontainers.boot.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={CassandraReactiveDataAutoConfiguration.class})
public class KafkaTest {

    @Autowired
    private KafkaConsumerWithQueue consumer;
    @Autowired
    private KafkaBootProducer producer;

    @Test
    public void testMessageSentMessageReceived() throws InterruptedException {
        //Given
        var message = "message";
        var key = 1;

        //When
        producer.sendMessage(key, message);

        //Then
        var consumedMessage = consumer.getQueue().poll(5, TimeUnit.SECONDS);

        assertNotNull(consumedMessage,
            String.format("Expected \"%s\" but none obtained.", message));
        assertEquals(message, consumedMessage,
            String.format("Expected \"%s\" but obtained \"%s\" instead.", message, consumedMessage));
    }

}
