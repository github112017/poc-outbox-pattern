package dev.lucasdeabreu.poc.outbox.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.lucasdeabreu.poc.outbox.outbox.AggregateType;
import dev.lucasdeabreu.poc.outbox.outbox.Message;
import dev.lucasdeabreu.poc.outbox.outbox.MessageType;

import java.util.UUID;

public class CreateOrderMessage implements Message {

	private static ObjectMapper mapper = new ObjectMapper();

	private final UUID aggregateId;

	private final JsonNode payload;

	private CreateOrderMessage(UUID aggregateId, JsonNode payload) {
		this.aggregateId = aggregateId;
		this.payload = payload;
	}

	static CreateOrderMessage of(final Order order) {
		ObjectNode node = mapper.createObjectNode().put("orderId", order.getId().toString())
				.put("clientId", order.getClientId().toString()).put("totalValue", order.getTotalValue());
		return new CreateOrderMessage(order.getId(), node);
	}

	@Override
	public AggregateType aggregateType() {
		return AggregateType.ORDER;
	}

	@Override
	public UUID aggregateId() {
		return aggregateId;
	}

	@Override
	public MessageType type() {
		return MessageType.CREATE;
	}

	@Override
	public JsonNode payload() {
		return payload;
	}

}
