package com.hotel.hotel.entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "room_types", uniqueConstraints =
        @UniqueConstraint(name = "uk_room_type_hotel_name", columnNames = {"hotel_id", "name"}))
public class RoomType {
    @Id
    private String id;
    @Column(name = "hotel_id", nullable = false, updatable = false)
    private String hotelId;
    @Column(nullable = false)
    private String name;
    @Column(length = 1000)
    private String description;
    @Column(name = "max_adults", nullable = false)
    private int maxAdults;
    @Column(name = "max_children", nullable = false)
    private int maxChildren;
    @Column(name = "base_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal basePrice;
    @Column(nullable = false, length = 3)
    private String currency;
}
