package com.github.rbaul.groupchat.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableConfigurationProperties({
		GroupChatProperties.class
})
@EnableAsync
@Configuration
public class GroupChatConfig {
}
