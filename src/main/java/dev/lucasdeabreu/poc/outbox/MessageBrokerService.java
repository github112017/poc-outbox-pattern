package dev.lucasdeabreu.poc.outbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageBrokerService {

	public void send(final String topic, final Object message) {
		log.debug("Sending to message broker, topic: {}, message: {}", topic, message);
	}

}
