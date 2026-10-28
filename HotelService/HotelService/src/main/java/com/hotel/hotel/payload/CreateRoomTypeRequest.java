package com.hotel.hotel.payload;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateRoomTypeRequest {
	@NotBlank
	private String name;
	private String description;
	@Min(1)
	private int maxAdults;
	@Min(0)
	private int maxChildren;
	@NotNull
	@DecimalMin(value = "0.0", inclusive = false)
	private BigDecimal basePrice;
	@NotBlank
	@Pattern(regexp = "[A-Z]{3}")
	private String currency;
}
