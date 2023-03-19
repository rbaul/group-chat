package com.github.rbaul.groupchat.cache;

import com.github.rbaul.groupchat.web.dto.SessionInfoDto;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@UtilityClass
public class SessionCache {
	private final ConcurrentHashMap<Long, Set<String>> roomIdToSessionId = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<String, SessionInfoDto> sessionToInfo = new ConcurrentHashMap<>();
	
	private final ConcurrentHashMap<String, Long> sessionToRoom = new ConcurrentHashMap<>();
	
	public void addSession(Long roomId) {
		addSession(roomId, null);
	}
	
	public void addSession(Long roomId, SessionInfoDto sessionInfoDto) {
		if (!roomIdToSessionId.containsKey(roomId)) {
			roomIdToSessionId.put(roomId, new HashSet<>());
		}
		if (sessionInfoDto != null) {
			roomIdToSessionId.get(roomId).add(sessionInfoDto.getId());
			sessionToRoom.put(sessionInfoDto.getId(), roomId);
			sessionToInfo.put(sessionInfoDto.getId(), sessionInfoDto);
		}
	}
	
	public void removeSession(String sessionId) {
		Long roomId = sessionToRoom.get(sessionId);
		if (roomId != null) {
			Set<String> sessionIds = roomIdToSessionId.get(roomId);
			boolean isRemoved = sessionIds.remove(sessionId);
			if (isRemoved) {
				sessionToInfo.remove(sessionId);
				if (sessionIds.isEmpty()) {
					roomIdToSessionId.remove(roomId);
				}
			}
		}
	}
	
	public Set<String> getRoomParticipants(Long roomId) {
		return roomIdToSessionId.getOrDefault(roomId, new HashSet<>()).stream()
				.map(sessionId -> sessionToInfo.get(sessionId).getUsername())
				.collect(Collectors.toSet());
	}
	
	public Long getRoomIdBySessionId(String sessionId) {
		return sessionToRoom.get(sessionId);
	}
}
