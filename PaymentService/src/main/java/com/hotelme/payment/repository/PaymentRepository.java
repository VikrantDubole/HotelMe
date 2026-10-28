package com.hotelme.payment.repository;
import com.hotelme.payment.domain.Payment; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface PaymentRepository extends JpaRepository<Payment,String>{Optional<Payment> findByIdempotencyKey(String key);List<Payment> findAllByGuestIdOrderByCreatedAtDesc(String guestId);}
