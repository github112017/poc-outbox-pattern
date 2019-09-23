package dev.lucasdeabreu.poc.outbox.outbox;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OutboxService {

	private final OutboxRepository outboxRepository;

	public void add(final Message event) {
		OutboxMessage message = OutboxMessage.builder().type(event.type()).aggregateId(event.aggregateId())
				.aggregateType(event.aggregateType()).payload(event.payload()).build();
		outboxRepository.save(message);
	}

}
