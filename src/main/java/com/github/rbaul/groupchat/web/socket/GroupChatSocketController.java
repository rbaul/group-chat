package com.github.rbaul.groupchat.web.socket;

import com.github.rbaul.groupchat.service.GroupChatService;
import com.github.rbaul.groupchat.web.dto.GroupChatMessageDto;
import com.github.rbaul.groupchat.web.dto.GroupChatMessageNotificationDto;
import com.github.rbaul.groupchat.web.dto.NotificationTypeDto;
import com.github.rbaul.groupchat.web.dto.SessionInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class GroupChatSocketController {
	
	private final GroupChatService groupChatService;
	
	@MessageMapping("/group-chats/{id}/message")
	@SendTo("/topic/group-chats/{id}/messages-changed")
	public GroupChatMessageNotificationDto newMessage(@DestinationVariable long id, @Payload GroupChatMessageDto message) {
		GroupChatMessageDto groupChatMessageDto = groupChatService.addNewMessage(id, message);
		return GroupChatMessageNotificationDto.builder()
				.notificationType(NotificationTypeDto.CREATE)
				.content(groupChatMessageDto).build();
	}
	
	@EventListener
	private void handleSessionConnected(SessionConnectedEvent event) {
		log.info("Connected: {}", event);
		GenericMessage<byte[]> message = (GenericMessage<byte[]>) event.getMessage();
		Map<String, List<String>> o = (Map<String, List<String>>) ((GenericMessage<byte[]>) message.getHeaders().get("simpConnectMessage")).getHeaders().get("nativeHeaders");
		if (o.containsKey("user")) {
			String user = o.get("user").get(0);
			Long roomId = Long.decode(o.get("roomId").get(0));
			SessionInfoDto simpSessionId = SessionInfoDto.builder().id((String) message.getHeaders().get("simpSessionId")).username(user).build();
			groupChatService.addSession(roomId, simpSessionId);
		}
	}
	
	@EventListener
	private void handleSessionConnected(SessionDisconnectEvent event) {
		String sessionId = event.getSessionId();
		groupChatService.removeSession(sessionId);
	}
}
