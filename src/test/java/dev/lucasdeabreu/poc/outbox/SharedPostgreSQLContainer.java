package dev.lucasdeabreu.poc.outbox;

import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.PostgreSQLContainer;

public class SharedPostgreSQLContainer extends PostgreSQLContainer {

	private static final String IMAGE_VERSION = "debezium/postgres:11";

	private static SharedPostgreSQLContainer container;

	private SharedPostgreSQLContainer() {
		super(IMAGE_VERSION);
	}

	public static SharedPostgreSQLContainer getInstance() {
		if (container == null) {
			container = new SharedPostgreSQLContainer();
		}
		return container;
	}

	@Override
	public void start() {
		super.start();
		System.setProperty("DB_URL", container.getJdbcUrl());
		System.setProperty("DB_USERNAME", container.getUsername());
		System.setProperty("DB_PASSWORD", container.getPassword());
		System.setProperty("DB_NAME", container.getDatabaseName());
		System.setProperty("DB_HOSTNAME", extractHostname());
		System.setProperty("DB_PORT", String.valueOf(container.getFirstMappedPort()));
	}

	@NotNull
	private String extractHostname() {
		return container.getJdbcUrl().replace("jdbc:postgresql://", "")
				.replace(":" + container.getFirstMappedPort() + "/" + container.getDatabaseName(), "");
	}

	@Override
	public void stop() {
	}

}
