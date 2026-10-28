package com.hotel.hotel.controllers;

import com.hotel.hotel.entites.Room;
import com.hotel.hotel.entites.RoomStatus;
import com.hotel.hotel.entites.RoomType;
import com.hotel.hotel.payload.CreateRoomRequest;
import com.hotel.hotel.payload.CreateRoomTypeRequest;
import com.hotel.hotel.services.RoomCatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hotels/{hotelId}")
public class RoomCatalogController {
    private final RoomCatalogService service;

    public RoomCatalogController(RoomCatalogService service) {
        this.service = service;
    }

    @PostMapping("/room-types")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<RoomType> createRoomType(@PathVariable String hotelId,
                                                   @Valid @RequestBody CreateRoomTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRoomType(hotelId, request));
    }

    @GetMapping("/room-types")
    public List<RoomType> getRoomTypes(@PathVariable String hotelId) {
        return service.getRoomTypes(hotelId);
    }

    @PostMapping("/rooms")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<Room> createRoom(@PathVariable String hotelId,
                                           @Valid @RequestBody CreateRoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRoom(hotelId, request));
    }

    @GetMapping("/rooms")
    public List<Room> getRooms(@PathVariable String hotelId,
                               @RequestParam(required = false) String roomTypeId,
                               @RequestParam(required = false) RoomStatus status) {
        return service.getRooms(hotelId, roomTypeId, status);
    }
}
