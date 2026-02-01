package com.travelbooking.travelbooking.mapper;
import com.travelbooking.travelbooking.dto.HotelRequestDto;
import com.travelbooking.travelbooking.dto.HotelResponseDto;
import com.travelbooking.travelbooking.entity.Hotel;

public class HotelMapper {

    public static Hotel toEntity(HotelRequestDto hotelRequestDto) {
       Hotel hotel = new Hotel();

       hotel.setHotelName(hotelRequestDto.getHotelName());
       hotel.setCity(hotelRequestDto.getCity());
       hotel.setStars(hotelRequestDto.getStars());
       hotel.setPricePerNight(hotelRequestDto.getPricePerNight());
       return hotel;
    }

    public static HotelResponseDto toDto(Hotel hotel) {
        HotelResponseDto hotelResponseDto = new HotelResponseDto();

        hotelResponseDto.setHotelName(hotel.getHotelName());
        hotelResponseDto.setCity(hotel.getCity());
        hotelResponseDto.setPricePerNight(hotel.getPricePerNight());
        return hotelResponseDto;
    }

}
