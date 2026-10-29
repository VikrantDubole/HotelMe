package com.hotel.booking.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler({ IllegalArgumentException.class, MethodArgumentNotValidException.class })
	ResponseEntity<Map<String, Object>> badRequest(Exception exception) {
		return response(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<Map<String, Object>> conflict(IllegalStateException exception) {
		return response(HttpStatus.CONFLICT, exception.getMessage());
	}

	@ExceptionHandler(NoSuchElementException.class)
	ResponseEntity<Map<String, Object>> notFound(NoSuchElementException exception) {
		return response(HttpStatus.NOT_FOUND, exception.getMessage());
	}

	@ExceptionHandler(SecurityException.class)
	ResponseEntity<Map<String, Object>> forbidden(SecurityException exception) {
		return response(HttpStatus.FORBIDDEN, exception.getMessage());
	}

	private ResponseEntity<Map<String, Object>> response(HttpStatus status, String message) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now());
		body.put("status", status.value());
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		return ResponseEntity.status(status).body(body);
	}
}
