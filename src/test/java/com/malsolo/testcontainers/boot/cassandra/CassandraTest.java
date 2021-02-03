package com.malsolo.testcontainers.boot.cassandra;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import java.net.InetSocketAddress;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class CassandraTest extends AbstractCassandraContainerBaseTest {

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
