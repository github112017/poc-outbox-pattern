package dev.lucasdeabreu.poc.outbox.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxMessage, UUID> {

	List<OutboxMessage> findAllByTypeAndAggregateTypeAndAggregateId(MessageType type, AggregateType aggregateType,
			UUID aggregateId);

}
