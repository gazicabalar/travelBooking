package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.BookingRequestDto;
import com.travelbooking.travelbooking.dto.BookingResponseDto;
import com.travelbooking.travelbooking.entity.*;
import com.travelbooking.travelbooking.exception.BusinessException;
import com.travelbooking.travelbooking.exception.ResourceNotFoundException;
import com.travelbooking.travelbooking.mapper.BookingMapper;
import com.travelbooking.travelbooking.repository.BookingRepository;
import com.travelbooking.travelbooking.repository.FlightRepository;
import com.travelbooking.travelbooking.repository.HotelRepository;
import com.travelbooking.travelbooking.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, HotelRepository hotelRepository, UserRepository userRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto) {
        User user = userRepository.findById(bookingRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Flight flight = flightRepository.findById(bookingRequestDto.getFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        Double totalPrice = calculateTotalPrice(flight, hotel);

         Booking booking = new Booking();
         booking.setUser(user);
         booking.setFlight(flight);
         booking.setHotel(hotel);
         booking.setStatus(BookingStatus.CREATED);
         booking.setBookingDate(LocalDateTime.now());
         booking.setTotalPrice(totalPrice);

         Booking savedBooking = bookingRepository.save(booking);
         return BookingMapper.toDto(savedBooking);
    }

    private Double calculateTotalPrice(Flight flight, Hotel hotel) {
        return flight.getPrice() + hotel.getPricePerNight();
    }

    @Override
    public BookingResponseDto getById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    @Override
    public BookingResponseDto confirmBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CREATED)
            throw new BusinessException("Only CREATED bookings can be confirmed");

        Flight flight = booking.getFlight();

        if (flight.getAvailableSeats() <= 0) {
            throw new BusinessException("No available seats for this flight");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        booking.setStatus(BookingStatus.CONFIRMED);

        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(updatedBooking);

    }

    @Override
    public BookingResponseDto cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.CONFIRMED)
            throw new BusinessException("Only CONFIRMED bookings can be cancelled");

        Flight flight = booking.getFlight();

        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);

        booking.setStatus(BookingStatus.CANCELLED);

        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(updatedBooking);
    }


}
