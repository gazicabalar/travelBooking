package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.FlightRequestDto;
import com.travelbooking.travelbooking.dto.FlightResponseDto;
import com.travelbooking.travelbooking.entity.Flight;
import com.travelbooking.travelbooking.mapper.FlightMapper;
import com.travelbooking.travelbooking.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService{

    private final FlightRepository flightRepository;

    private static final int DEFAULT_SEAT_CAPACITY = 150;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public FlightResponseDto createFlight(FlightRequestDto flightRequestDto) {
        Flight flight = FlightMapper.toEntity(flightRequestDto);
        flight.setAvailableSeats(DEFAULT_SEAT_CAPACITY);
        Flight savedFlight = flightRepository.save(flight);
        return FlightMapper.toDto(savedFlight);
    }

    @Override
    public FlightResponseDto getById(Long flightId) {
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new RuntimeException("Flight not found"));
        return FlightMapper.toDto(flight);
    }

    @Override
    public List<FlightResponseDto> getAllFlights() {
        return flightRepository.findAll()
                .stream()
                .map(FlightMapper::toDto)
                .toList();
    }
}
