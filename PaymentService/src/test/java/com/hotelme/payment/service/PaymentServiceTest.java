package com.hotelme.payment.service;

import com.hotelme.payment.api.ReservationSnapshot;
import com.hotelme.payment.domain.*;
import com.hotelme.payment.gateway.PaymentGateway;
import com.hotelme.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
	@Test
	void derivesAmountFromBookingServiceAndMakesRetryIdempotent() {
		PaymentRepository repository = mock(PaymentRepository.class);
		PaymentGateway gateway = mock(PaymentGateway.class);
		RestTemplate rest = mock(RestTemplate.class);
		ReservationSnapshot snapshot = new ReservationSnapshot();
		snapshot.setId("booking-1");
		snapshot.setGuestId("guest-1");
		snapshot.setTotalAmount(new BigDecimal("450.00"));
		snapshot.setCurrency("USD");
		snapshot.setStatus("CONFIRMED");
		when(repository.findByIdempotencyKey("pay-key")).thenReturn(Optional.empty());
		when(rest.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(ReservationSnapshot.class)))
				.thenReturn(ResponseEntity.ok(snapshot));
		when(gateway.charge(new BigDecimal("450.00"), "USD")).thenReturn("sim_123");
		when(repository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));
		Payment result = new PaymentService(repository, gateway, rest).pay("booking-1", "guest-1", "pay-key",
				"Bearer token");
		assertEquals(new BigDecimal("450.00"), result.getAmount());
		assertEquals(PaymentStatus.SUCCEEDED, result.getStatus());
		assertEquals("sim_123", result.getProviderReference());
	}

	@Test
	void rejectsPaymentForAnotherGuest() {
		PaymentRepository repository = mock(PaymentRepository.class);
		PaymentGateway gateway = mock(PaymentGateway.class);
		RestTemplate rest = mock(RestTemplate.class);
		ReservationSnapshot snapshot = new ReservationSnapshot();
		snapshot.setGuestId("other");
		snapshot.setStatus("CONFIRMED");
		when(repository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());
		when(rest.exchange(anyString(), any(), any(), eq(ReservationSnapshot.class)))
				.thenReturn(ResponseEntity.ok(snapshot));
		assertThrows(SecurityException.class,
				() -> new PaymentService(repository, gateway, rest).pay("booking-1", "guest-1", "key", "Bearer token"));
		verify(gateway, never()).charge(any(), any());
	}
}
