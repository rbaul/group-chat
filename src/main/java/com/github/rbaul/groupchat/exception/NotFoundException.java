package com.github.rbaul.groupchat.exception;

public class NotFoundException extends GroupChatException {
	public NotFoundException(String message) {
		super(message);
	}
	
	public NotFoundException() {
		super();
	}
	
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
