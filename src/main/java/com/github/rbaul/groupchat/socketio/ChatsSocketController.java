package com.github.rbaul.groupchat.socketio;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatsSocketController {
	
	public static final String CHATS_NAMESPACE = "/chats";
	private final SocketIONamespace namespace;
	
	public ChatsSocketController(SocketIOServer server) {
		this.namespace = server.addNamespace(CHATS_NAMESPACE);
		this.namespace.addConnectListener(onConnected());
		this.namespace.addDisconnectListener(onDisconnected());
	}
	
	private ConnectListener onConnected() {
		return client -> {
			HandshakeData handshakeData = client.getHandshakeData();
			log.debug("Client[{}] - Connected to chat module through '{}'", client.getSessionId().toString(), handshakeData.getUrl());
		};
	}
	
	private DisconnectListener onDisconnected() {
		return client -> {
			log.debug("Client[{}] - Disconnected from chat module.", client.getSessionId().toString());
//			groupChatService.removeSession(client.getSessionId().toString());
		};
	}
}
