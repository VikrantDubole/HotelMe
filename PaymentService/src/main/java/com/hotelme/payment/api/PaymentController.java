package com.hotelme.payment.api;
import com.hotelme.payment.domain.Payment; import com.hotelme.payment.service.PaymentService; import org.springframework.http.*; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.security.oauth2.jwt.Jwt; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/payments")
public class PaymentController {
 
 private final PaymentService service; public PaymentController(PaymentService service){this.service=service;}
 
 @PostMapping 
 public ResponseEntity<Payment> pay(@RequestParam String reservationId,@RequestHeader("Idempotency-Key") String key,@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,@AuthenticationPrincipal Jwt jwt){return ResponseEntity.status(HttpStatus.CREATED).body(service.pay(reservationId,jwt.getSubject(),key,authorization));}
 
 @GetMapping 
 public List<Payment> mine(@AuthenticationPrincipal Jwt jwt){return service.mine(jwt.getSubject());}
 
 @PostMapping("/{id}/refund") 
 public Payment refund(@PathVariable String id,@AuthenticationPrincipal Jwt jwt){return service.refund(id,jwt.getSubject());}
}
