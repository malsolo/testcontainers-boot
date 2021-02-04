package com.malsolo.testcontainers.boot.cassandra;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base class for tests that need Cassandra.
 * See https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/.
 * See https://rieckpil.de/reuse-containers-with-testcontainers-for-fast-integration-tests/.
 * See https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-testcontainers.
 */
@Slf4j
@DataCassandraTest
@SuppressWarnings("rawtypes")
public class AbstractCassandraContainerBaseTest {

    private static final String CASSANDRA_IMAGE_NAME = "datastax/dse-server:6.8.9";
    private static final int CASSANDRA_IMAGE_PORT = 9042;

    static final String LOCAL_DATACENTER = "dc1";

    static String CASSANDRA_HOST;
    static Integer CASSANDRA_PORT;

    static final GenericContainer cassandra;

    static {
        log.warn("***** Create Cassandra container.");
        cassandra = new GenericContainer(DockerImageName.parse(CASSANDRA_IMAGE_NAME))
            .withEnv("DS_LICENSE", "accept")
            .withExposedPorts(CASSANDRA_IMAGE_PORT);
        log.warn("***** About to start Cassandra.");
        cassandra.start();
        CASSANDRA_HOST = cassandra.getHost();
        CASSANDRA_PORT = cassandra.getMappedPort(CASSANDRA_IMAGE_PORT);
        log.warn("***** Cassandra started, available at {}:{} with local datacenter {}.",
            CASSANDRA_HOST, CASSANDRA_PORT, LOCAL_DATACENTER);
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.data.cassandra.contact-points", () -> CASSANDRA_HOST);
        registry.add("spring.data.cassandra.port", () -> CASSANDRA_PORT);
        registry.add("spring.data.cassandra.local-datacenter", () -> LOCAL_DATACENTER);
    }

}
