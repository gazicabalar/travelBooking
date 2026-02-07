package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.HotelRequestDto;
import com.travelbooking.travelbooking.dto.HotelResponseDto;
import com.travelbooking.travelbooking.entity.Hotel;
import com.travelbooking.travelbooking.mapper.HotelMapper;
import com.travelbooking.travelbooking.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;

    private static final int DEFAULT_ROOM_CAPACITY = 100;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public HotelResponseDto createHotel(HotelRequestDto hotelRequestDto) {
        Hotel hotel = HotelMapper.toEntity(hotelRequestDto);
        hotel.setAvailableRooms(DEFAULT_ROOM_CAPACITY);
        Hotel savedHotel = hotelRepository.save(hotel);
        return HotelMapper.toDto(savedHotel);
    }

    @Override
    public HotelResponseDto getById(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new RuntimeException("Hotel not found"));
        return HotelMapper.toDto(hotel);
    }

    @Override
    public List<HotelResponseDto> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(HotelMapper::toDto)
                .toList();
    }
}
