package com.travelbooking.travelbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequestDto {

    @NotBlank(message = "Hotel name cannot be blank")
    private String hotelName;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotNull(message = "Stars cannot be null")
    @Positive(message = "Stars must be positive")
    private int stars;

    @NotNull(message = "Price per night cannot be null")
    @Positive(message = "Price must be positive")
    private Double pricePerNight;

}
