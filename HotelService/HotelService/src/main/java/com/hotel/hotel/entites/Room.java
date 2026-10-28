package com.lcwd.hotel.entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms", uniqueConstraints =
        @UniqueConstraint(name = "uk_room_hotel_number", columnNames = {"hotel_id", "room_number"}))
public class Room {
    @Id
    private String id;
    @Column(name = "hotel_id", nullable = false, updatable = false)
    private String hotelId;
    @Column(name = "room_type_id", nullable = false, updatable = false)
    private String roomTypeId;
    @Column(name = "room_number", nullable = false)
    private String roomNumber;
    private Integer floor;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;
}
