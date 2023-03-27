package com.github.rbaul.groupchat.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.github.rbaul.groupchat.cache.SessionCache;
import com.github.rbaul.groupchat.service.GroupChatService;
import com.github.rbaul.groupchat.web.dto.GroupChatMessageDto;
import com.github.rbaul.groupchat.web.dto.SessionInfoDto;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocketIoStarter implements ApplicationListener<ApplicationReadyEvent> {
	
	private final SocketIOServer socketIOServer;
	private final GroupChatService groupChatService;
	
	private SocketIONamespace chatNameSpace;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		socketIOServer.start();
	}
	
	@PreDestroy
	public void stopSocketIOServer() {
		socketIOServer.stop();
	}
	
	@OnDisconnect
	void onDisconnect(SocketIOClient client) {
		groupChatService.removeSession(client.getSessionId().toString());
		log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
	}
	
	@OnConnect
	public void onConnect(SocketIOClient client) {
		String room = client.getHandshakeData().getSingleUrlParam("room");
		if (StringUtils.hasText(room)) {
			client.joinRoom(room);
			
			groupChatService.addSession(Long.valueOf(room), SessionInfoDto.builder()
					.id(client.getSessionId().toString())
					.username(client.getHandshakeData().getSingleUrlParam("user")).build());
		}
		log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
	}
	
	@OnEvent(value = "newMessage")
	public void newMessage(SocketIOClient client, AckRequest request, GroupChatMessageDto message) {
		Long id = SessionCache.getRoomIdBySessionId(client.getSessionId().toString());
		GroupChatMessageDto groupChatMessageDto = groupChatService.addNewMessage(id, message);
	}
}
