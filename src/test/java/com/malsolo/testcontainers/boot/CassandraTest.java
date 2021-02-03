package com.malsolo.testcontainers.boot;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import java.net.InetSocketAddress;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class CassandraTest {

    public static final String CASSANDRA_IMAGE_NAME = "datastax/dse-server:6.8.9";
    public static final int CASSANDRA_IMAGE_PORT = 9042;
    private static final String LOCAL_DATACENTER = "dc1";

    private static String CASSANDRA_HOST;
    private static Integer CASSANDRA_PORT;

    @Container
    @SuppressWarnings("rawtypes")
    public static GenericContainer cassandra = new GenericContainer(DockerImageName.parse(CASSANDRA_IMAGE_NAME))
        .withEnv("DS_LICENSE", "accept")
        .withExposedPorts(CASSANDRA_IMAGE_PORT);

    @BeforeAll
    public static void setUp() {
        CASSANDRA_HOST = cassandra.getHost();
        CASSANDRA_PORT = cassandra.getMappedPort(CASSANDRA_IMAGE_PORT);
    }

    @Test
    public void test(){
        System.out.printf("Cassandra contact point %s", CASSANDRA_HOST);
        System.out.printf("Cassandra contact port: %d", CASSANDRA_PORT);
    }

    @Test
    public void testCassandraConnection() {
        try (CqlSession session = CqlSession.builder()
            .addContactPoint(new InetSocketAddress(CASSANDRA_HOST, CASSANDRA_PORT))
            .withLocalDatacenter(LOCAL_DATACENTER)
            .build()) {
            ResultSet rs = session.execute("select release_version from system.local");
            Row row = rs.one();
            System.out.println(Optional.ofNullable(row.getString("release_version")).orElse("No release"));
        }
    }

}
