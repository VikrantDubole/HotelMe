package com.hotel.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.booking.domain.Reservation;

import java.util.*;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
	Optional<Reservation> findByIdempotencyKey(String key);

	List<Reservation> findAllByGuestIdOrderByCreatedAtDesc(String guestId);
}
