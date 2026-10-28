package com.lcwd.hotel.respositories;

import com.lcwd.hotel.entites.Room;
import com.lcwd.hotel.entites.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findAllByHotelIdOrderByRoomNumber(String hotelId);
    List<Room> findAllByHotelIdAndRoomTypeId(String hotelId, String roomTypeId);
    List<Room> findAllByHotelIdAndStatus(String hotelId, RoomStatus status);
    List<Room> findAllByHotelIdAndRoomTypeIdAndStatus(String hotelId, String roomTypeId, RoomStatus status);
    boolean existsByHotelIdAndRoomNumber(String hotelId, String roomNumber);
}
