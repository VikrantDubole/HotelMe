package com.lcwd.hotel.payload;

import com.lcwd.hotel.entites.RoomStatus;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateRoomRequest {
    @NotBlank private String roomTypeId;
    @NotBlank private String roomNumber;
    private Integer floor;
    @NotNull private RoomStatus status;
}
