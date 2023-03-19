package com.github.rbaul.groupchat.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
public class GroupChat {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotEmpty
	@Column(unique = true)
	private String name;
	
	private String description;
	
	@Version
	@Getter(AccessLevel.PRIVATE)
	@Setter(AccessLevel.PRIVATE)
	private Short version;
	
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "groupChat")
	private List<GroupChatMessage> messages = new ArrayList<>();
	
	public void setMessages(List<GroupChatMessage> histories) {
		if (!CollectionUtils.isEmpty(this.messages)) {
			this.messages.forEach(this::removeHistoryMessage);
		}
		
		if (!CollectionUtils.isEmpty(histories)) {
			histories.forEach(this::addHistoryMessage);
		}
	}
	
	public void addHistoryMessage(GroupChatMessage historyMessage) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(historyMessage);
		historyMessage.setGroupChat(this);
	}
	
	public void removeHistoryMessage(GroupChatMessage historyMessage) {
		if (!CollectionUtils.isEmpty(messages)) {
			messages.remove(historyMessage);
			historyMessage.setGroupChat(null);
		}
	}
}
