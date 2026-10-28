package com.hotelme.payment.api;
import lombok.Getter; import lombok.Setter; import java.math.BigDecimal;
@Getter @Setter public class ReservationSnapshot { private String id; private String guestId; private BigDecimal totalAmount; private String currency; private String status; }
