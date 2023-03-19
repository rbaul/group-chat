package com.github.rbaul.groupchat.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class GroupChatMessage {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotEmpty
	private String userName;
	@NotEmpty
	private String message;
	
	@Column(columnDefinition = "TIMESTAMP")
	@CreationTimestamp
	private ZonedDateTime dateTime;
	
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	private GroupChat groupChat;
}
