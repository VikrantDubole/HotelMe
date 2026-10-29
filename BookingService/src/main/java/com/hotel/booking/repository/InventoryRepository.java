package com.hotel.booking.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.hotel.booking.domain.Inventory;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.*;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select i from Inventory i where i.hotelId=:hotelId and i.roomTypeId=:roomTypeId and i.stayDate>=:start and i.stayDate<:end order by i.stayDate")
	List<Inventory> lockStay(@Param("hotelId") String hotelId, @Param("roomTypeId") String roomTypeId,
			@Param("start") LocalDate start, @Param("end") LocalDate end);

	@Query("select i from Inventory i where i.hotelId=:hotelId and i.roomTypeId=:roomTypeId and i.stayDate>=:start and i.stayDate<:end order by i.stayDate")
	List<Inventory> findStay(@Param("hotelId") String hotelId, @Param("roomTypeId") String roomTypeId,
			@Param("start") LocalDate start, @Param("end") LocalDate end);

	Optional<Inventory> findByHotelIdAndRoomTypeIdAndStayDate(String hotelId, String roomTypeId, LocalDate stayDate);
}
