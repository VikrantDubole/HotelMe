package com.hotel.payment.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.hotel.payment.api.ReservationSnapshot;
import com.hotel.payment.domain.*;
import com.hotel.payment.gateway.PaymentGateway;
import com.hotel.payment.repository.PaymentRepository;

import java.time.Instant;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PaymentService {
	private final PaymentRepository repository;
	private final PaymentGateway gateway;
	private final RestTemplate restTemplate;

	public PaymentService(PaymentRepository repository, PaymentGateway gateway, RestTemplate restTemplate) {
		this.repository = repository;
		this.gateway = gateway;
		this.restTemplate = restTemplate;
	}

	@Transactional
	public Payment pay(String reservationId, String guestId, String key, String authorization) {
		if (key == null || key.isBlank())
			throw new IllegalArgumentException("Idempotency-Key header is required");
		Optional<Payment> existing = repository.findByIdempotencyKey(key);
		if (existing.isPresent())
			return existing.get();
		ReservationSnapshot reservation = reservation(reservationId, authorization);
		if (!guestId.equals(reservation.getGuestId()))
			throw new SecurityException("Reservation belongs to another guest");
		if (!"CONFIRMED".equals(reservation.getStatus()))
			throw new IllegalStateException("Only confirmed reservations can be paid");
		Payment payment = new Payment();
		payment.setId(UUID.randomUUID().toString());
		payment.setReservationId(reservationId);
		payment.setGuestId(guestId);
		payment.setAmount(reservation.getTotalAmount());
		payment.setCurrency(reservation.getCurrency());
		payment.setStatus(PaymentStatus.PROCESSING);
		payment.setIdempotencyKey(key);
		payment.setCreatedAt(Instant.now());
		payment.setUpdatedAt(Instant.now());
		payment.setProviderReference(gateway.charge(payment.getAmount(), payment.getCurrency()));
		payment.setStatus(PaymentStatus.SUCCEEDED);
		return repository.save(payment);
	}

	public List<Payment> mine(String guestId) {
		return repository.findAllByGuestIdOrderByCreatedAtDesc(guestId);
	}

	@Transactional
	public Payment refund(String id, String guestId) {
		Payment p = repository.findById(id).orElseThrow(() -> new NoSuchElementException("Payment not found"));
		if (!p.getGuestId().equals(guestId))
			throw new SecurityException("Payment belongs to another guest");
		if (p.getStatus() != PaymentStatus.SUCCEEDED)
			throw new IllegalStateException("Only successful payments can be refunded");
		gateway.refund(p.getProviderReference(), p.getAmount(), p.getCurrency());
		p.setStatus(PaymentStatus.REFUNDED);
		p.setUpdatedAt(Instant.now());
		return repository.save(p);
	}

	private ReservationSnapshot reservation(String id, String authorization) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.AUTHORIZATION, authorization);
		ResponseEntity<ReservationSnapshot> response = restTemplate.exchange(
				"http://BOOKING-SERVICE/reservations/" + id, HttpMethod.GET, new HttpEntity<>(headers),
				ReservationSnapshot.class);
		return Objects.requireNonNull(response.getBody(), "Booking Service returned an empty reservation");
	}
}
