package dev.lucasdeabreu.poc.outbox.cdc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.lucasdeabreu.poc.outbox.MessageBrokerService;
import io.debezium.config.Configuration;
import io.debezium.data.Envelope.Operation;
import io.debezium.embedded.EmbeddedEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static io.debezium.data.Envelope.FieldName.AFTER;
import static io.debezium.data.Envelope.FieldName.OPERATION;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
public class OutboxMessageWatcher {

	private final Executor executor = Executors.newSingleThreadExecutor();

	private final EmbeddedEngine engine;

	private final MessageBrokerService messageBrokerService;

	public OutboxMessageWatcher(Configuration debeziumConfiguration, MessageBrokerService messageBrokerService) {
		this.engine = EmbeddedEngine.create().using(debeziumConfiguration).notifying(this::handleEvent).build();
		this.messageBrokerService = messageBrokerService;
	}

	@PostConstruct
	void postConstruct() {
		executor.execute(engine);
	}

	@PreDestroy
	void stop() {
		if (engine != null) {
			engine.stop();
		}
	}

	private void handleEvent(SourceRecord sourceRecord) {
		Struct sourceRecordValue = (Struct) sourceRecord.value();
		Operation operation = Operation.forCode((String) sourceRecordValue.get(OPERATION));
		if (operation == Operation.CREATE) {
			Struct after = (Struct) sourceRecordValue.get(AFTER);
			Map<String, String> message = after.schema().fields().stream().map(Field::name)
					.map(fieldName -> Pair.of(fieldName, after.getString(fieldName)))
					.collect(toMap(Pair::getKey, Pair::getValue));
			log.debug("Sending to message broker: {}", message);
			messageBrokerService.send("TOPIC", message);
		}
	}

}
