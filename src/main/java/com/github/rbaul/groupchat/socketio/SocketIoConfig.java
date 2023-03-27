package com.github.rbaul.groupchat.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIoConfig {
	
	@Bean
	public SocketIOServer socketIOServer(SocketIoProperties socketIoProperties) {
		return new SocketIOServer(socketIoProperties.getConfiguration());
	}
	
	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
		return new SpringAnnotationScanner(socketIOServer);
	}
}
