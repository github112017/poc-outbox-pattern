package dev.lucasdeabreu.poc.outbox.outbox;

import dev.lucasdeabreu.poc.outbox.MessageBrokerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboxService {

	private final OutboxRepository outboxRepository;
	private final MessageBrokerService messageBrokerService;

	public void add(final Message event) {
		OutboxMessage message = OutboxMessage.builder().type(event.type()).aggregateId(event.aggregateId())
				.aggregateType(event.aggregateType()).payload(event.payload()).build();
		outboxRepository.save(message);
	}

	@Transactional
	public void sendOutboxMessagesToBroker() {
		outboxRepository.findAll().forEach(this::sendOutboxMessageToBroker);
	}

	private void sendOutboxMessageToBroker(OutboxMessage outboxMessage) {
		log.debug("Sending message");
		messageBrokerService.send("ANY_TOPIC", outboxMessage.getPayload());
		log.debug("Deleting outbox message from database, id: {}", outboxMessage.getId());
		outboxRepository.deleteById(outboxMessage.getId());
	}


}
