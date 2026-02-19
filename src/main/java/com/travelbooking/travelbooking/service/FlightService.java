package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.FlightRequestDto;
import com.travelbooking.travelbooking.dto.FlightResponseDto;

import java.util.List;

public interface FlightService {
    FlightResponseDto createFlight(FlightRequestDto flightRequestDto);

    FlightResponseDto getById(Long flightId);

    String deleteFlight(Long flightId);

    List<FlightResponseDto> getAllFlights();
}
