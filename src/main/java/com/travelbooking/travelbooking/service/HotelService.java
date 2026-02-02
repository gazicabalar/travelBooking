package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.HotelRequestDto;
import com.travelbooking.travelbooking.dto.HotelResponseDto;

import java.util.List;

public interface HotelService {
    HotelResponseDto createHotel(HotelRequestDto hotelRequestDto);

    HotelResponseDto getById(Long hotelId);

    List<HotelResponseDto> getAllHotels();
}
