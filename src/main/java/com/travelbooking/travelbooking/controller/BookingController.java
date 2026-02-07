package com.travelbooking.travelbooking.controller;

import com.travelbooking.travelbooking.dto.BookingRequestDto;
import com.travelbooking.travelbooking.dto.BookingResponseDto;
import com.travelbooking.travelbooking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto bookingRequestDto) {
        BookingResponseDto response = bookingService.createBooking(bookingRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/confirm")
    public ResponseEntity<BookingResponseDto> confirmBooking(@PathVariable Long bookingId) {
        BookingResponseDto response = bookingService.confirmBooking(bookingId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponseDto> cancelBooking(@PathVariable Long bookingId) {
        BookingResponseDto response = bookingService.cancelBooking(bookingId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllBookings")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        List<BookingResponseDto> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/getById/{bookingId}")
    public ResponseEntity<BookingResponseDto> getById(@PathVariable Long bookingId) {
        BookingResponseDto booking = bookingService.getById(bookingId);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

}
