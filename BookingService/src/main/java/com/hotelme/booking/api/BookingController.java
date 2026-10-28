package com.hotelme.booking.api;

import com.hotelme.booking.api.BookingRequests.*; import com.hotelme.booking.domain.Reservation; import com.hotelme.booking.service.BookingService;
import org.springframework.http.*; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.security.core.Authentication; import org.springframework.security.core.annotation.AuthenticationPrincipal; import org.springframework.security.oauth2.jwt.Jwt; import org.springframework.web.bind.annotation.*;
import javax.validation.Valid; import java.time.LocalDate; import java.util.List; import java.util.Map;

@RestController
public class BookingController {
    private final BookingService service; public BookingController(BookingService service){this.service=service;}
    @PutMapping("/inventory") @PreAuthorize("hasAuthority('Admin')") public ResponseEntity<Void> inventory(@Valid @RequestBody InventoryRequest r){service.setInventory(r);return ResponseEntity.noContent().build();}
    @GetMapping("/availability") public Map<String,Object> availability(@RequestParam String hotelId,@RequestParam String roomTypeId,@RequestParam LocalDate checkIn,@RequestParam LocalDate checkOut,@RequestParam(defaultValue="1") int rooms){return service.availability(hotelId,roomTypeId,checkIn,checkOut,rooms);}
    @PostMapping("/reservations") public ResponseEntity<Reservation> reserve(@AuthenticationPrincipal Jwt jwt,@RequestHeader("Idempotency-Key") String key,@Valid @RequestBody ReservationRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(service.reserve(jwt.getSubject(),key,r));}
    @GetMapping("/reservations") public List<Reservation> mine(@AuthenticationPrincipal Jwt jwt){return service.getForGuest(jwt.getSubject());}
    @GetMapping("/reservations/{id}") public Reservation get(@PathVariable String id,@AuthenticationPrincipal Jwt jwt,Authentication authentication){return service.get(id,jwt.getSubject(),authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("Admin")));}
    @PostMapping("/reservations/{id}/cancel") public Reservation cancel(@PathVariable String id,@AuthenticationPrincipal Jwt jwt,Authentication authentication){return service.cancel(id,jwt.getSubject(),authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("Admin")));}
}
