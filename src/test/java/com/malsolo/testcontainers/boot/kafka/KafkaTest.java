package com.malsolo.testcontainers.boot.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@EnableAutoConfiguration(exclude={CassandraReactiveDataAutoConfiguration.class,
    CassandraAutoConfiguration.class, CassandraDataAutoConfiguration.class})
@Testcontainers
@Slf4j
public class KafkaTest {

    public static final String KAFKA_IMAGE_NAME = "confluentinc/cp-kafka:6.0.1";

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse(KAFKA_IMAGE_NAME));

    @Autowired
    private KafkaConsumerWithQueue consumer;
    @Autowired
    private KafkaBootProducer producer;

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        var index = kafka.getBootstrapServers().lastIndexOf('/');
        index = index == -1 ? 0 : index + 1;
        var bootstrapServers = kafka.getBootstrapServers().substring(index);
        log.warn("\n\nspring.kafka.bootstrap-servers={}\n\n", bootstrapServers);
        registry.add("spring.kafka.bootstrap-servers", () -> bootstrapServers);
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
    }

    @Test
    public void testMessageSentMessageReceived() throws InterruptedException {
        //Given
        var message = "message";
        var key = 1;

        //When
        producer.sendMessage(key, message);
        Thread.sleep(1000);

        //Then
        var consumedMessage = consumer.getQueue().poll(5, TimeUnit.SECONDS);

        assertNotNull(consumedMessage,
            String.format("Expected \"%s\" but none obtained.", message));
        assertEquals(message, consumedMessage,
            String.format("Expected \"%s\" but obtained \"%s\" instead.", message, consumedMessage));
    }

}
