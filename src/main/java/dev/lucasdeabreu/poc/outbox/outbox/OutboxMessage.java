package dev.lucasdeabreu.poc.outbox.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonNodeBinaryType.class)
@Entity
@Table(name = "outbox_messages")
public class OutboxMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Enumerated(EnumType.STRING)
	private MessageType type;

	@Enumerated(EnumType.STRING)
	private AggregateType aggregateType;

	private UUID aggregateId;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private JsonNode payload;

}
