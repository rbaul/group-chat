package com.github.rbaul.groupchat.service;

import com.github.rbaul.groupchat.web.dto.GroupChatMessageNotificationDto;
import com.github.rbaul.groupchat.web.dto.GroupChatNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebSocketUpdaterService {
	
	private final SimpMessagingTemplate template;
	
	@Async
	public void notifyGroupChatMessageChange(long groupChatId, GroupChatMessageNotificationDto notificationDto) {
		template.convertAndSend(String.format("/topic/group-chats/%d/messages-changed", groupChatId), notificationDto);
	}
	
	@Async
	public void notifyGroupChatsChange(long groupChatId, GroupChatNotificationDto notificationDto) {
		template.convertAndSend("/topic/group-chats/changed", notificationDto);
	}
}
