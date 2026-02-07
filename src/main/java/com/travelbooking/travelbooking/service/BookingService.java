package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.BookingRequestDto;
import com.travelbooking.travelbooking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto);

    BookingResponseDto getById(Long bookingId);

    List<BookingResponseDto> getAllBookings();

    BookingResponseDto confirmBooking(Long bookingId);

    BookingResponseDto cancelBooking(Long bookingId);

}
