package com.github.rbaul.groupchat.service;

import com.github.rbaul.groupchat.cache.SessionCache;
import com.github.rbaul.groupchat.domain.model.GroupChat;
import com.github.rbaul.groupchat.domain.model.GroupChatMessage;
import com.github.rbaul.groupchat.domain.repository.GroupChatMessageRepository;
import com.github.rbaul.groupchat.domain.repository.GroupChatRepository;
import com.github.rbaul.groupchat.web.dto.GroupChatDto;
import com.github.rbaul.groupchat.web.dto.GroupChatMessageDto;
import com.github.rbaul.groupchat.web.dto.GroupChatNotificationDto;
import com.github.rbaul.groupchat.web.dto.NotificationTypeDto;
import com.github.rbaul.groupchat.web.dto.SessionInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupChatService {
	
	private final GroupChatRepository groupChatRepository;
	
	private final GroupChatMessageRepository groupChatMessageRepository;
	
	private final ModelMapper modelMapper;
	
	private final WebSocketUpdaterService webSocketUpdaterService;
	
	public GroupChatDto get(long id) {
		return converterGroupChatToDto(getById(id));
	}
	
	public GroupChatDto create(GroupChatDto groupChatDto) {
		GroupChat groupChat = groupChatRepository.save(modelMapper.map(groupChatDto, GroupChat.class));
		
		GroupChatDto dto = modelMapper.map(groupChat, GroupChatDto.class);
		SessionCache.addSession(dto.getId());
		webSocketUpdaterService.notifyGroupChatsChange(dto.getId(), GroupChatNotificationDto.builder()
				.notificationType(NotificationTypeDto.CREATE)
				.content(dto).build());
		return dto;
	}
	
	@Transactional
	public GroupChatDto update(long id, GroupChatDto groupChatDto) {
		GroupChat groupChat = getById(id);
		groupChat.setName(groupChatDto.getName());
		groupChat.setDescription(groupChatDto.getDescription());
		GroupChatDto dto = modelMapper.map(groupChat, GroupChatDto.class);
		webSocketUpdaterService.notifyGroupChatsChange(dto.getId(), GroupChatNotificationDto.builder()
				.notificationType(NotificationTypeDto.UPDATE)
				.content(dto).build());
		return dto;
	}
	
	public void delete(long id) {
		groupChatRepository.deleteById(id);
		webSocketUpdaterService.notifyGroupChatsChange(id, GroupChatNotificationDto.builder()
				.notificationType(NotificationTypeDto.DELETE).build());
	}
	
	public List<GroupChatDto> getAll() {
		return groupChatRepository.findAll().stream()
				.map(this::converterGroupChatToDto)
				.collect(Collectors.toList());
	}
	
	private GroupChatDto converterGroupChatToDto(GroupChat groupChat) {
		GroupChatDto groupChatDto = modelMapper.map(groupChat, GroupChatDto.class);
		groupChatDto.setParticipants(SessionCache.getRoomParticipants(groupChatDto.getId()));
		return groupChatDto;
	}
	
	public Page<GroupChatDto> search(String filter, Pageable pageable) {
		return groupChatRepository.findAll(pageable)
				.map(this::converterGroupChatToDto);
	}
	
	private GroupChat getById(long id) {
		return groupChatRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException("No found group chat with id: " + id, 1));
	}
	
	public List<GroupChatMessageDto> getGroupChatHistory(long id) {
		return groupChatMessageRepository.findByGroupChatId(id).stream()
				.map(groupChatMessage -> modelMapper.map(groupChatMessage, GroupChatMessageDto.class))
				.collect(Collectors.toList());
	}
	
	@Transactional
	public GroupChatMessageDto addNewMessage(long id, GroupChatMessageDto groupChatMessageDto) {
		GroupChat groupChat = getById(id);
		GroupChatMessage groupChatMessage = modelMapper.map(groupChatMessageDto, GroupChatMessage.class);
		groupChat.addHistoryMessage(groupChatMessage);
		return modelMapper.map(groupChatMessage, GroupChatMessageDto.class);
	}
	
	public void addSession(Long roomId, SessionInfoDto sessionInfoDto) {
		SessionCache.addSession(roomId, sessionInfoDto);
		GroupChatDto groupChatDto = get(roomId);
		webSocketUpdaterService.notifyGroupChatsChange(groupChatDto.getId(), GroupChatNotificationDto.builder()
				.notificationType(NotificationTypeDto.UPDATE)
				.content(groupChatDto).build());
	}
	
	public void removeSession(String sessionId) {
		Long roomId = SessionCache.getRoomIdBySessionId(sessionId);
		if (roomId != null) {
			SessionCache.removeSession(sessionId);
			GroupChatDto groupChatDto = get(roomId);
			webSocketUpdaterService.notifyGroupChatsChange(groupChatDto.getId(), GroupChatNotificationDto.builder()
					.notificationType(NotificationTypeDto.UPDATE)
					.content(groupChatDto).build());
		}
	}
}
