package com.hotelme.booking.repository;

import com.hotelme.booking.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
	Optional<Reservation> findByIdempotencyKey(String key);

	List<Reservation> findAllByGuestIdOrderByCreatedAtDesc(String guestId);
}
