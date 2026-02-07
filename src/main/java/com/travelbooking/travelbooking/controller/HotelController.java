package com.travelbooking.travelbooking.controller;

import com.travelbooking.travelbooking.dto.HotelRequestDto;
import com.travelbooking.travelbooking.dto.HotelResponseDto;
import com.travelbooking.travelbooking.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping("/createHotel")
    public ResponseEntity<HotelResponseDto> createHotel(@RequestBody HotelRequestDto hotelRequestDto) {
        HotelResponseDto hotelResponseDto = hotelService.createHotel(hotelRequestDto);
        return new ResponseEntity<>(hotelResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelResponseDto> getById(@PathVariable Long hotelId) {
        HotelResponseDto hotelResponseDto = hotelService.getById(hotelId);
        return new ResponseEntity<>(hotelResponseDto, HttpStatus.OK);
    }

    @GetMapping("getAllHotels")
    public ResponseEntity<List<HotelResponseDto>> getAllHotels() {
        List<HotelResponseDto> hotelResponseDto = hotelService.getAllHotels();
        return new ResponseEntity<>(hotelResponseDto, HttpStatus.OK);
    }

}
