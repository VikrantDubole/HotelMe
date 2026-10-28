package com.lcwd.hotel.services;

import com.lcwd.hotel.entites.Room;
import com.lcwd.hotel.entites.RoomStatus;
import com.lcwd.hotel.entites.RoomType;
import com.lcwd.hotel.exceptions.ConflictException;
import com.lcwd.hotel.exceptions.ResourceNotFoundException;
import com.lcwd.hotel.payload.CreateRoomRequest;
import com.lcwd.hotel.payload.CreateRoomTypeRequest;
import com.lcwd.hotel.respositories.HotelRepository;
import com.lcwd.hotel.respositories.RoomRepository;
import com.lcwd.hotel.respositories.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RoomCatalogService {
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;

    public RoomCatalogService(HotelRepository hotelRepository, RoomTypeRepository roomTypeRepository,
                              RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomType createRoomType(String hotelId, CreateRoomTypeRequest request) {
        requireHotel(hotelId);
        if (roomTypeRepository.existsByHotelIdAndNameIgnoreCase(hotelId, request.getName())) {
            throw new ConflictException("A room type with this name already exists for the hotel");
        }
        return roomTypeRepository.save(new RoomType(UUID.randomUUID().toString(), hotelId,
                request.getName().trim(), request.getDescription(), request.getMaxAdults(),
                request.getMaxChildren(), request.getBasePrice(), request.getCurrency().toUpperCase()));
    }

    public List<RoomType> getRoomTypes(String hotelId) {
        requireHotel(hotelId);
        return roomTypeRepository.findAllByHotelIdOrderByName(hotelId);
    }

    @Transactional
    public Room createRoom(String hotelId, CreateRoomRequest request) {
        requireHotel(hotelId);
        RoomType type = roomTypeRepository.findById(request.getRoomTypeId())
                .filter(candidate -> candidate.getHotelId().equals(hotelId))
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found for hotel: " + hotelId));
        if (roomRepository.existsByHotelIdAndRoomNumber(hotelId, request.getRoomNumber())) {
            throw new ConflictException("Room number already exists for the hotel");
        }
        return roomRepository.save(new Room(UUID.randomUUID().toString(), hotelId, type.getId(),
                request.getRoomNumber().trim(), request.getFloor(), request.getStatus()));
    }

    public List<Room> getRooms(String hotelId, String roomTypeId, RoomStatus status) {
        requireHotel(hotelId);
        if (roomTypeId != null && status != null) {
            return roomRepository.findAllByHotelIdAndRoomTypeIdAndStatus(hotelId, roomTypeId, status);
        }
        if (roomTypeId != null) {
            return roomRepository.findAllByHotelIdAndRoomTypeId(hotelId, roomTypeId);
        }
        if (status != null) {
            return roomRepository.findAllByHotelIdAndStatus(hotelId, status);
        }
        return roomRepository.findAllByHotelIdOrderByRoomNumber(hotelId);
    }

    private void requireHotel(String hotelId) {
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found: " + hotelId);
        }
    }
}
