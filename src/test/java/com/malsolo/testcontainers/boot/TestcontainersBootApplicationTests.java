package com.malsolo.testcontainers.boot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude={CassandraReactiveDataAutoConfiguration.class})
class TestcontainersBootApplicationTests {

	@Test
	void contextLoads() {
	}

}
