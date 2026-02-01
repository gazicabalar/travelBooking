package com.travelbooking.travelbooking.mapper;

import com.travelbooking.travelbooking.dto.BookingResponseDto;
import com.travelbooking.travelbooking.entity.Booking;

public class BookingMapper {

    public static BookingResponseDto toDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setBookingId(booking.getBookingId());
        bookingResponseDto.setUserId(booking.getUser().getUserId());
        bookingResponseDto.setHotelId(booking.getHotel().getHotelId());
        bookingResponseDto.setFlightId(booking.getFlight().getFlightId());
        bookingResponseDto.setStatus(booking.getStatus().name());
        bookingResponseDto.setCreatedAt(booking.getCreatedAt());
        return bookingResponseDto;
    }

}
