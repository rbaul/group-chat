package com.github.rbaul.groupchat.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class GroupChatDto {
	
	@NotNull(groups = ValidationGroups.Update.class)
	private Long id;
	
	@NotEmpty
	private String name;
	
	private String description;
	
	private Set<String> participants;
	
}
