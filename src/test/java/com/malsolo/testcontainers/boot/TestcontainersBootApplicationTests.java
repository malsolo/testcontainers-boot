package com.malsolo.testcontainers.boot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={CassandraReactiveDataAutoConfiguration.class,
		CassandraAutoConfiguration.class, CassandraDataAutoConfiguration.class})
class TestcontainersBootApplicationTests {

	@Test
	void contextLoads() {
	}

}
