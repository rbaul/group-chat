package com.github.rbaul.groupchat.domain.repository;

import com.github.rbaul.groupchat.domain.model.GroupChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, Long> {
	List<GroupChatMessage> findByGroupChatId(long id);
}
