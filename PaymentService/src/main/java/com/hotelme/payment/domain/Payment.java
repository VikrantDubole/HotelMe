package com.hotelme.payment.domain;
import lombok.Getter; import lombok.NoArgsConstructor; import lombok.Setter; import javax.persistence.*; import java.math.BigDecimal; import java.time.Instant;
@Entity @Table(name="payments",uniqueConstraints=@UniqueConstraint(name="uk_payment_idempotency",columnNames="idempotency_key")) @Getter @Setter @NoArgsConstructor
public class Payment {
 @Id private String id; @Column(name="reservation_id",nullable=false) private String reservationId; @Column(name="guest_id",nullable=false) private String guestId;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal amount; @Column(nullable=false,length=3) private String currency;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private PaymentStatus status; @Column(name="provider_reference",nullable=false) private String providerReference;
 @Column(name="idempotency_key",nullable=false,updatable=false) private String idempotencyKey; @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt; @Column(name="updated_at",nullable=false) private Instant updatedAt; @Version private long version;
}
