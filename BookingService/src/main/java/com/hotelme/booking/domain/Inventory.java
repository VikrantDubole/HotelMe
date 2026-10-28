package com.hotelme.booking.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "room_inventory", uniqueConstraints = @UniqueConstraint(name = "uk_inventory_day", columnNames = {
		"hotel_id", "room_type_id", "stay_date" }))
@Getter
@Setter
@NoArgsConstructor
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "hotel_id", nullable = false)
	private String hotelId;
	@Column(name = "room_type_id", nullable = false)
	private String roomTypeId;
	@Column(name = "stay_date", nullable = false)
	private LocalDate stayDate;
	@Column(name = "total_rooms", nullable = false)
	private int totalRooms;
	@Column(name = "available_rooms", nullable = false)
	private int availableRooms;
	@Column(name = "nightly_rate", nullable = false, precision = 12, scale = 2)
	private BigDecimal nightlyRate;
	@Column(nullable = false, length = 3)
	private String currency;
	@Version
	private long version;
}
