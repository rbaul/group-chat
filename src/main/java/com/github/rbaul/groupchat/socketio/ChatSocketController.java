package com.github.rbaul.groupchat.socketio;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.github.rbaul.groupchat.cache.SessionCache;
import com.github.rbaul.groupchat.service.GroupChatService;
import com.github.rbaul.groupchat.web.dto.GroupChatMessageDto;
import com.github.rbaul.groupchat.web.dto.SessionInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class ChatSocketController {
	
	public static final String CHAT_NAMESPACE = "/chat";
	@Autowired
	private GroupChatService groupChatService;
	
	private final SocketIONamespace namespace;
	
	public ChatSocketController(SocketIOServer server) {
		this.namespace = server.addNamespace(CHAT_NAMESPACE);
		this.namespace.addConnectListener(onConnected());
		this.namespace.addDisconnectListener(onDisconnected());
		this.namespace.addEventListener("newMessage", GroupChatMessageDto.class, onChatReceived());
	}
	
	private DataListener<GroupChatMessageDto> onChatReceived() {
		return (client, data, ackSender) -> {
			String sessionId = client.getSessionId().toString();
			log.debug("Client[{}] - Received chat message '{}'", sessionId, data);
//			namespace.getBroadcastOperations().sendEvent("chat", data);
			Long id = SessionCache.getRoomIdBySessionId(sessionId);
			if (id == null) {
				log.error("Not found room ID, on session: " + sessionId);
			} else {
				GroupChatMessageDto groupChatMessageDto = groupChatService.addNewMessage(id, data);
			}
		};
	}
	
	private ConnectListener onConnected() {
		return client -> {
			HandshakeData handshakeData = client.getHandshakeData();
			log.debug("Client[{}] - Connected to chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
			String room = client.getHandshakeData().getSingleUrlParam("room");
			if (StringUtils.hasText(room)) {
				client.joinRoom(room);
				
				groupChatService.addSession(Long.valueOf(room), SessionInfoDto.builder()
						.id(client.getSessionId().toString())
						.username(client.getHandshakeData().getSingleUrlParam("user")).build());
			}
			log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
			
			
		};
	}
	
	private DisconnectListener onDisconnected() {
		return client -> {
			log.debug("Client[{}] - Disconnected from chat module.", client.getSessionId().toString());
			groupChatService.removeSession(client.getSessionId().toString());
		};
	}
}
