package dev.lucasdeabreu.poc.outbox.order;

import dev.lucasdeabreu.poc.outbox.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;

	private final OutboxService outboxService;

	@Transactional
	public Order save(final Order order) {
		Order saved = orderRepository.save(order);
		outboxService.add(CreateOrderMessage.of(order));
		return saved;
	}

}
