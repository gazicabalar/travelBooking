package com.travelbooking.travelbooking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Flight ID cannot be null")
    private Long flightId;

    @NotNull(message = "Hotel ID cannot be null")
    private Long hotelId;

}
