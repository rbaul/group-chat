package com.github.rbaul.groupchat.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class GroupChatNotificationDto extends NotificationDto<GroupChatDto> {
	
}