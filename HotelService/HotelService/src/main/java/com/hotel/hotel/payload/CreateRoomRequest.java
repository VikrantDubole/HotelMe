package com.hotel.hotel.payload;

import com.hotel.hotel.entites.RoomStatus;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateRoomRequest {
	@NotBlank
	private String roomTypeId;
	@NotBlank
	private String roomNumber;
	private Integer floor;
	@NotNull
	private RoomStatus status;
}
