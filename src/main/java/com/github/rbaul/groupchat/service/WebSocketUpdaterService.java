package com.github.rbaul.groupchat.service;

import com.github.rbaul.groupchat.web.dto.GroupChatMessageNotificationDto;
import com.github.rbaul.groupchat.web.dto.GroupChatNotificationDto;
import org.springframework.scheduling.annotation.Async;

public interface WebSocketUpdaterService {
	
	@Async
	default void notifyGroupChatMessageChange(long groupChatId, GroupChatMessageNotificationDto notificationDto) {
	}
	
	@Async
	default void notifyGroupChatsChange(long groupChatId, GroupChatNotificationDto notificationDto) {
	}
}
