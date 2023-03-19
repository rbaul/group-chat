package com.github.rbaul.groupchat.exception;

public class GroupChatException extends RuntimeException {
	public GroupChatException(String message) {
		super(message);
	}
	
	public GroupChatException() {
		super();
	}
	
	public GroupChatException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public GroupChatException(Throwable cause) {
		super(cause);
	}
}
