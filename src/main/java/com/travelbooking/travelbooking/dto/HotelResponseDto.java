package com.travelbooking.travelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponseDto {
    private Long hotelId;
    private String hotelName;
    private String city;
    private int stars;
    private Double pricePerNight;
    private int availableRooms;
}
