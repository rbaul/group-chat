package com.github.rbaul.groupchat.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.github.rbaul.groupchat.service.WebSocketUpdaterService;
import com.github.rbaul.groupchat.web.dto.GroupChatMessageNotificationDto;
import com.github.rbaul.groupchat.web.dto.GroupChatNotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SocketIoUpdaterService implements WebSocketUpdaterService {
	
	private final SocketIOServer socketIOServer;
	
	@Override
	public void notifyGroupChatMessageChange(long groupChatId, GroupChatMessageNotificationDto notificationDto) {
		this.socketIOServer.getRoomOperations(String.valueOf(groupChatId)).sendEvent("chat-message-change", notificationDto);
	}
	
	@Override
	public void notifyGroupChatsChange(long groupChatId, GroupChatNotificationDto notificationDto) {
		this.socketIOServer.getBroadcastOperations().sendEvent("group-chats-change", notificationDto);
	}
}
