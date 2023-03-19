package com.github.rbaul.groupchat.domain.repository;

import com.github.rbaul.groupchat.domain.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
	Optional<GroupChat> findByName(String name);
}
