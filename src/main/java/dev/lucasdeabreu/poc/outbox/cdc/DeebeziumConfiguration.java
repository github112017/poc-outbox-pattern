package dev.lucasdeabreu.poc.outbox.cdc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeebeziumConfiguration {

	@Bean
	public io.debezium.config.Configuration debeziumConfiguration(final DatasourceProperties properties) {
		return io.debezium.config.Configuration.create()
				.with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
				.with("offset.storage", "org.apache.kafka.connect.storage.MemoryOffsetBackingStore")
				.with("offset.flush.interval.ms", 60000).with("name", "outbox-postgres-connector")
				.with("database.server.name", "pg-outbox-server").with("database.hostname", properties.getHostname())
				.with("database.port", properties.getPort()).with("database.user", properties.getUsername())
				.with("database.password", properties.getPassword()).with("database.dbname", properties.getName())
				.with("table.whitelist", "public.outbox_messages").build();
	}

}