package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.HotelRequestDto;
import com.travelbooking.travelbooking.dto.HotelResponseDto;
import com.travelbooking.travelbooking.entity.Hotel;
import com.travelbooking.travelbooking.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("HotelServiceImpl Testleri")
public class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelRequestDto hotelRequestDto;

    @BeforeEach
    void setUp(){
        hotel = Hotel.builder()
                .hotelId(1L)
                .hotelName("Ramada")
                .city("İstanbul")
                .pricePerNight(5000.0)
                .availableRooms(120)
                .stars(5)
                .build();

        hotelRequestDto = new HotelRequestDto();
        hotelRequestDto.setHotelName(hotel.getHotelName());
        hotelRequestDto.setCity(hotel.getCity());
        hotelRequestDto.setStars(hotel.getStars());
        hotelRequestDto.setPricePerNight(hotel.getPricePerNight());
    }

    @Test
    @DisplayName("Otel başarıyla oluşturulmalı")
    void shouldCreateHotelSuccessfully(){
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        HotelResponseDto result = hotelService.createHotel(hotelRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getHotelId()).isEqualTo(hotel.getHotelId());
        assertThat(result.getHotelName()).isEqualTo(hotel.getHotelName());
        assertThat(result.getCity()).isEqualTo(hotel.getCity());
        assertThat(result.getStars()).isEqualTo(hotel.getStars());
        assertThat(result.getPricePerNight()).isEqualTo(hotel.getPricePerNight());
    }

    @Test
    @DisplayName("Otel ID'sine göre başarıyla getirilmeli")
    void shouldGetHotelByIdSuccessfully(){
        when(hotelRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(hotel));

        HotelResponseDto result = hotelService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getHotelId()).isEqualTo(hotel.getHotelId());
        assertThat(result.getHotelName()).isEqualTo(hotel.getHotelName());
        assertThat(result.getCity()).isEqualTo(hotel.getCity());
        assertThat(result.getStars()).isEqualTo(hotel.getStars());
        assertThat(result.getPricePerNight()).isEqualTo(hotel.getPricePerNight());
    }

    @Test
    @DisplayName("Tüm oteller başarıyla getirilmeli")
    void shouldGetAllHotelsSuccessfully(){
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        List<HotelResponseDto> result = hotelService.getAllHotels();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Otel başarıyla güncellenmeli")
    void shouldUpdateHotelSuccessfully(){
        when(hotelRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        HotelResponseDto result = hotelService.updateHotel(1L, hotelRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getHotelId()).isEqualTo(hotel.getHotelId());
        assertThat(result.getHotelName()).isEqualTo(hotel.getHotelName());
        assertThat(result.getCity()).isEqualTo(hotel.getCity());
        assertThat(result.getStars()).isEqualTo(hotel.getStars());
        assertThat(result.getPricePerNight()).isEqualTo(hotel.getPricePerNight());
    }
    @Test
    @DisplayName("Otel başarıyla silinmeli")
    void shouldDeleteHotelSuccessfully(){
        when(hotelRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(hotel));

        String result = hotelService.deleteHotel(1L);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("Hotel deleted successfully : " + 1L);
    }

}
