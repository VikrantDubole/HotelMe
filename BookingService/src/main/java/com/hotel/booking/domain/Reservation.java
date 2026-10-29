package com.hotel.booking.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.*;

@Entity
@Table(name = "reservations", uniqueConstraints = @UniqueConstraint(name = "uk_reservation_idempotency", columnNames = "idempotency_key"))
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
	@Id
	private String id;
	@Column(name = "guest_id", nullable = false)
	private String guestId;
	@Column(name = "hotel_id", nullable = false)
	private String hotelId;
	@Column(name = "room_type_id", nullable = false)
	private String roomTypeId;
	@Column(name = "check_in", nullable = false)
	private LocalDate checkIn;
	@Column(name = "check_out", nullable = false)
	private LocalDate checkOut;
	@Column(name = "room_count", nullable = false)
	private int roomCount;
	@Column(name = "adult_count", nullable = false)
	private int adults;
	@Column(name = "child_count", nullable = false)
	private int children;
	@Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal totalAmount;
	@Column(nullable = false, length = 3)
	private String currency;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReservationStatus status;
	@Column(name = "idempotency_key", nullable = false, updatable = false)
	private String idempotencyKey;
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	@Version
	private long version;
}
