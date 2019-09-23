package dev.lucasdeabreu.poc.outbox.cdc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class DatasourceProperties {

	private String url;

	private String username;

	private String password;

	private String name;

	private String hostname;

	private String port;

}
