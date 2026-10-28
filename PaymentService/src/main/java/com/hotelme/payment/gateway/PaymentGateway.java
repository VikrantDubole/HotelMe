package com.hotelme.payment.gateway;
import java.math.BigDecimal;
public interface PaymentGateway { String charge(BigDecimal amount,String currency); void refund(String providerReference,BigDecimal amount,String currency); }
