package dev.lucasdeabreu.poc.outbox.outbox;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public interface Message {

	AggregateType aggregateType();

	UUID aggregateId();

	MessageType type();

	JsonNode payload();

}
