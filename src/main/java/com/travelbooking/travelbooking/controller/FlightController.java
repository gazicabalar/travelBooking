package com.travelbooking.travelbooking.controller;

import com.travelbooking.travelbooking.dto.FlightRequestDto;
import com.travelbooking.travelbooking.dto.FlightResponseDto;
import com.travelbooking.travelbooking.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/create")
    public ResponseEntity<FlightResponseDto> createFlight(@Valid @RequestBody FlightRequestDto flightRequestDto) {
        FlightResponseDto flightResponseDto = flightService.createFlight(flightRequestDto);
        return new ResponseEntity<>(flightResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<FlightResponseDto> getById(@PathVariable Long flightId) {
        FlightResponseDto flightResponseDto = flightService.getById(flightId);
        return new ResponseEntity<>(flightResponseDto, HttpStatus.OK);
    }

    @GetMapping("/getAllFlights")
    public ResponseEntity<List<FlightResponseDto>> getAllFlights() {
        List<FlightResponseDto> flightResponseDto = flightService.getAllFlights();
        return new ResponseEntity<>(flightResponseDto, HttpStatus.OK);
    }

}
