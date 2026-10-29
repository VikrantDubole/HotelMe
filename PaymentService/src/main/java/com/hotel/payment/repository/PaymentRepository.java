package com.hotel.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.payment.domain.Payment;

import java.util.*;

public interface PaymentRepository extends JpaRepository<Payment, String> {
	Optional<Payment> findByIdempotencyKey(String key);

	List<Payment> findAllByGuestIdOrderByCreatedAtDesc(String guestId);
}
