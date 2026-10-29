package com.hotel.booking.service;

import org.junit.jupiter.api.Test;

import com.hotel.booking.api.BookingRequests.ReservationRequest;
import com.hotel.booking.domain.Inventory;
import com.hotel.booking.domain.Reservation;
import com.hotel.booking.repository.InventoryRepository;
import com.hotel.booking.repository.ReservationRepository;
import com.hotel.booking.service.BookingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {
	@Test
	void reservesEveryNightAtomicallyAndCalculatesServerSideTotal() {
		InventoryRepository inventoryRepository = mock(InventoryRepository.class);
		ReservationRepository reservationRepository = mock(ReservationRepository.class);
		LocalDate checkIn = LocalDate.now().plusDays(2);
		Inventory first = day(checkIn, 3, "125.00");
		Inventory second = day(checkIn.plusDays(1), 3, "150.00");
		when(reservationRepository.findByIdempotencyKey("key-1")).thenReturn(Optional.empty());
		when(inventoryRepository.lockStay("hotel-1", "type-1", checkIn, checkIn.plusDays(2)))
				.thenReturn(List.of(first, second));
		when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Reservation result = new BookingService(inventoryRepository, reservationRepository).reserve("guest-1", "key-1",
				request(checkIn, 2));

		assertEquals(new BigDecimal("550.00"), result.getTotalAmount());
		assertEquals(1, first.getAvailableRooms());
		assertEquals(1, second.getAvailableRooms());
	}

	@Test
	void rejectsStayWhenAnyNightIsUnavailable() {
		InventoryRepository inventoryRepository = mock(InventoryRepository.class);
		ReservationRepository reservationRepository = mock(ReservationRepository.class);
		LocalDate checkIn = LocalDate.now().plusDays(2);
		when(reservationRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
		when(inventoryRepository.lockStay(anyString(), anyString(), any(), any()))
				.thenReturn(List.of(day(checkIn, 2, "100.00"), day(checkIn.plusDays(1), 0, "100.00")));

		assertThrows(IllegalStateException.class, () -> new BookingService(inventoryRepository, reservationRepository)
				.reserve("guest-1", "key-2", request(checkIn, 1)));
		verify(reservationRepository, never()).save(any());
	}

	private ReservationRequest request(LocalDate checkIn, int rooms) {
		ReservationRequest r = new ReservationRequest();
		r.setHotelId("hotel-1");
		r.setRoomTypeId("type-1");
		r.setCheckIn(checkIn);
		r.setCheckOut(checkIn.plusDays(2));
		r.setRooms(rooms);
		r.setAdults(2);
		return r;
	}

	private Inventory day(LocalDate date, int available, String rate) {
		Inventory i = new Inventory();
		i.setHotelId("hotel-1");
		i.setRoomTypeId("type-1");
		i.setStayDate(date);
		i.setTotalRooms(3);
		i.setAvailableRooms(available);
		i.setNightlyRate(new BigDecimal(rate));
		i.setCurrency("USD");
		return i;
	}
}
