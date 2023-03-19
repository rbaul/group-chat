package com.github.rbaul.groupchat.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class GroupChatMessageDto {
	
	@NotNull(groups = ValidationGroups.Update.class)
	private Long id;
	
	@NotEmpty
	private String userName;
	@NotEmpty
	private String message;
	
	private ZonedDateTime dateTime;
}
