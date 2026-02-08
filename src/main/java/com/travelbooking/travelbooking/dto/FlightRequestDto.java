package com.travelbooking.travelbooking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequestDto {

    @NotBlank(message = "From city cannot be blank")
    private String fromCity;

    @NotBlank(message = "To city cannot be blank")
    private String toCity;

    @NotNull(message = "Flight date cannot be null")
    private LocalDate flightDate;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Double price;

}
