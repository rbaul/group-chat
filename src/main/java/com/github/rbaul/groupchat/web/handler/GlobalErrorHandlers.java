package com.github.rbaul.groupchat.web.handler;

import com.github.rbaul.groupchat.web.dto.errors.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * It is recommended to replace the messages with those
 * that do not reveal details about the code.
 */
@Slf4j
@RestControllerAdvice
public class GlobalErrorHandlers {
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorDto handleGlobalError(Exception ex) {
		log.error("Global error handler exception: ", ex);
		return ErrorDto.builder()
				.errorCode("UNKNOWN")
				.message(ex.getLocalizedMessage())
				.build();
	}
}