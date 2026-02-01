package com.travelbooking.travelbooking.mapper;

import com.travelbooking.travelbooking.dto.FlightRequestDto;
import com.travelbooking.travelbooking.dto.FlightResponseDto;
import com.travelbooking.travelbooking.entity.Flight;

public class FlightMapper {

    public static Flight toEntity(FlightRequestDto flightRequestDto) {
        Flight flight = new Flight();
        flight.setFromCity(flightRequestDto.getFromCity());
        flight.setToCity(flightRequestDto.getToCity());
        flight.setFlightDate(flightRequestDto.getFlightDate());
        flight.setPrice(flightRequestDto.getPrice());
        return flight;
    }

    public static FlightResponseDto toDto(Flight flight) {
        FlightResponseDto flightResponseDto = new FlightResponseDto();
        flightResponseDto.setFlightId(flight.getFlightId());
        flightResponseDto.setFromCity(flight.getFromCity());
        flightResponseDto.setToCity(flight.getToCity());
        flightResponseDto.setFlightDate(flight.getFlightDate());
        flightResponseDto.setPrice(flight.getPrice());

        return flightResponseDto;
    }

}
