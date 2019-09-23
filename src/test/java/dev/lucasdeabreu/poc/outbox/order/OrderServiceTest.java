package dev.lucasdeabreu.poc.outbox.order;

import com.fasterxml.jackson.databind.JsonNode;
import dev.lucasdeabreu.poc.outbox.MessageBrokerService;
import dev.lucasdeabreu.poc.outbox.SharedPostgreSQLContainer;
import dev.lucasdeabreu.poc.outbox.outbox.AggregateType;
import dev.lucasdeabreu.poc.outbox.outbox.MessageType;
import dev.lucasdeabreu.poc.outbox.outbox.OutboxMessage;
import dev.lucasdeabreu.poc.outbox.outbox.OutboxRepository;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @ClassRule
    public static final PostgreSQLContainer POSTGRE_SQL_CONTAINER = SharedPostgreSQLContainer.getInstance();

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OutboxRepository outboxRepository;

    @MockBean
    private MessageBrokerService messageBrokerService;

    @Test
    public void shouldSaveOutboxMessageWhenOrderIsSaved() {
        Order order = orderService.save(newOrder());

        assertThat(orderRepository.existsById(order.getId())).isTrue();

        List<OutboxMessage> messages = outboxRepository.findAllByTypeAndAggregateTypeAndAggregateId(MessageType.CREATE,
                AggregateType.ORDER, order.getId());

        assertThat(messages).hasSize(1);
        JsonNode payload = messages.get(0).getPayload();
        assertThat(payload.get("orderId").asText()).isEqualTo(order.getId().toString());
        assertThat(payload.get("clientId").asText()).isEqualTo(order.getClientId().toString());
        assertThat(payload.get("totalValue").asDouble()).isEqualTo(order.getTotalValue());
    }

    @Test
    public void shouldSendMessageToBrokerWhenOrderIsSaved() {
        Order order = orderService.save(newOrder());

        verify(messageBrokerService, timeout(1000).times(1)).send(any(), any());
    }

    private Order newOrder() {
        return Order.builder().clientId(UUID.randomUUID()).description("Order test").totalValue(666D).build();
    }

}