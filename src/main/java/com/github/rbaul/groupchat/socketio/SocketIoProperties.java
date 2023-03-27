package com.github.rbaul.groupchat.socketio;

import com.corundumstudio.socketio.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "socket-io")
public class SocketIoProperties {
	
	private Configuration configuration = new Configuration();
}
