package dev.lucasdeabreu.poc.outbox;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OutboxServiceApplicationTests {

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = SharedPostgreSQLContainer.getInstance();

	@Test
	public void contextLoads() {
	}

}
