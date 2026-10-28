package com.hotelme.payment.gateway;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "payment.provider", havingValue = "simulated", matchIfMissing = true)
public class SimulatedPaymentGateway implements PaymentGateway {
	public String charge(BigDecimal amount, String currency) {
		return "sim_" + UUID.randomUUID();
	}

	public void refund(String reference, BigDecimal amount, String currency) {
	}
}
