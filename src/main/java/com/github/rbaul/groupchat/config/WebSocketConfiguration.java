package com.github.rbaul.groupchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/group-chat-websocket")
				.setAllowedOriginPatterns("*")
                .withSockJS();
    }
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
//		config.setApplicationDestinationPrefixes("/app");
	}
	
	@Bean
	public WebSocketHandler webSocketHandler() {
		return new AbstractWebSocketHandler() {
			@Override
			public void afterConnectionEstablished(WebSocketSession session) throws Exception {
				super.afterConnectionEstablished(session);
			}
			
			@Override
			public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
				super.handleMessage(session, message);
			}
			
			@Override
			public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
				super.afterConnectionClosed(session, status);
			}
		};
	}
}