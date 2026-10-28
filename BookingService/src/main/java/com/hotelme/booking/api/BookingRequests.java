package com.hotelme.booking.api;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class BookingRequests {
	private BookingRequests() {
	}

	@Getter
	@Setter
	public static class InventoryRequest {
		@NotBlank
		private String hotelId;
		@NotBlank
		private String roomTypeId;
		@NotNull
		@FutureOrPresent
		private LocalDate startDate;
		@NotNull
		private LocalDate endDate;
		@Min(0)
		private int totalRooms;
		@NotNull
		@DecimalMin("0.01")
		private BigDecimal nightlyRate;
		@NotBlank
		@Pattern(regexp = "[A-Z]{3}")
		private String currency;
	}

	@Getter
	@Setter
	public static class ReservationRequest {
		@NotBlank
		private String hotelId;
		@NotBlank
		private String roomTypeId;
		@NotNull
		@FutureOrPresent
		private LocalDate checkIn;
		@NotNull
		private LocalDate checkOut;
		@Min(1)
		private int rooms;
		@Min(1)
		private int adults;
		@Min(0)
		private int children;
	}
}
