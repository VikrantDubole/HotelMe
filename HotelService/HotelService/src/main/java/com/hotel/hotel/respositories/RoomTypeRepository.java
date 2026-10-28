package com.lcwd.hotel.respositories;

import com.lcwd.hotel.entites.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomTypeRepository extends JpaRepository<RoomType, String> {
    List<RoomType> findAllByHotelIdOrderByName(String hotelId);
    boolean existsByHotelIdAndNameIgnoreCase(String hotelId, String name);
}
