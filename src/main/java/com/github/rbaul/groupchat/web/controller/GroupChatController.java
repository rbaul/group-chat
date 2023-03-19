package com.github.rbaul.groupchat.web.controller;

import com.github.rbaul.groupchat.service.GroupChatService;
import com.github.rbaul.groupchat.web.dto.GroupChatDto;
import com.github.rbaul.groupchat.web.dto.GroupChatMessageDto;
import com.github.rbaul.groupchat.web.dto.ValidationGroups;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group-chats")
public class GroupChatController {
	
	private final GroupChatService groupChatService;
	
	@GetMapping("/{id}")
	public GroupChatDto getGroupChat(@PathVariable("id") long id) {
		return groupChatService.get(id);
	}
	
	@GetMapping("/{id}/history")
	public List<GroupChatMessageDto> getGroupChatHistory(@PathVariable("id") long id) {
		return groupChatService.getGroupChatHistory(id);
	}
	
	@PostMapping
	public GroupChatDto create(
			@Validated(ValidationGroups.Create.class) @RequestBody GroupChatDto customerDto) {
		return groupChatService.create(customerDto);
	}
	
	@PutMapping("/{id}")
	public GroupChatDto update(@PathVariable("id") long id,
							   @Validated(ValidationGroups.Update.class) @RequestBody GroupChatDto customerDto) {
		return groupChatService.update(id, customerDto);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable("id") long id) {
		groupChatService.delete(id);
	}
	
	@GetMapping("/search")
	public Page<GroupChatDto> fetch(@RequestParam(required = false) String filter, @PageableDefault Pageable pageable) {
		return groupChatService.search(filter, pageable);
	}
}
