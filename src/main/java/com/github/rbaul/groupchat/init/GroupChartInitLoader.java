package com.github.rbaul.groupchat.init;

import com.github.rbaul.groupchat.service.GroupChatService;
import com.github.rbaul.groupchat.web.dto.GroupChatDto;
import com.github.rbaul.groupchat.web.dto.GroupChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GroupChartInitLoader {
	
	@Bean
	public ApplicationListener<ApplicationReadyEvent> initialLoader(GroupChatService groupChatService) {
		return event -> {
			GroupChatDto groupChatDto = groupChatService.create(GroupChatDto.builder()
					.name("default")
					.description("Default group chat").build());
			
			groupChatService.addNewMessage(groupChatDto.getId(), GroupChatMessageDto.builder()
					.userName("user").message("Hello!").build());
			
			GroupChatDto groupChatDto2 = groupChatService.create(GroupChatDto.builder()
					.name("default_2")
					.description("Default group chat 2").build());
			
			groupChatService.addNewMessage(groupChatDto2.getId(), GroupChatMessageDto.builder()
					.userName("user").message("Hello!").build());
		};
	}
}
