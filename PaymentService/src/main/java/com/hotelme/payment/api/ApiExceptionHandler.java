package com.hotelme.payment.api;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<Map<String, Object>> bad(IllegalArgumentException e) {
		return error(HttpStatus.BAD_REQUEST, e);
	}

	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<Map<String, Object>> conflict(IllegalStateException e) {
		return error(HttpStatus.CONFLICT, e);
	}

	@ExceptionHandler(NoSuchElementException.class)
	ResponseEntity<Map<String, Object>> missing(NoSuchElementException e) {
		return error(HttpStatus.NOT_FOUND, e);
	}

	@ExceptionHandler(SecurityException.class)
	ResponseEntity<Map<String, Object>> forbidden(SecurityException e) {
		return error(HttpStatus.FORBIDDEN, e);
	}

	private ResponseEntity<Map<String, Object>> error(HttpStatus s, Exception e) {
		return ResponseEntity.status(s).body(Map.of("timestamp", Instant.now(), "status", s.value(), "error",
				s.getReasonPhrase(), "message", e.getMessage()));
	}
}
