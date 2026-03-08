package com.travelbooking.travelbooking.service;

import com.travelbooking.travelbooking.dto.BookingRequestDto;
import com.travelbooking.travelbooking.dto.BookingResponseDto;
import com.travelbooking.travelbooking.entity.*;
import com.travelbooking.travelbooking.exception.BusinessException;
import com.travelbooking.travelbooking.exception.ResourceNotFoundException;
import com.travelbooking.travelbooking.repository.BookingRepository;
import com.travelbooking.travelbooking.repository.FlightRepository;
import com.travelbooking.travelbooking.repository.HotelRepository;
import com.travelbooking.travelbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingServiceImpl Testleri")
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    private User user;
    private Flight flight;
    private Hotel hotel;
    private Booking booking;
    private BookingRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("pass");
        user.setCreatedDate(LocalDateTime.now());
        user.setRole(Role.ADMIN);

        flight = new Flight();
        flight.setFlightId(1L);
        flight.setFromCity("Istanbul");
        flight.setToCity("Ankara");
        flight.setFlightDate(LocalDate.now().plusDays(5));
        flight.setPrice(500.0);
        flight.setAvailableSeats(10);

        hotel = new Hotel();
        hotel.setHotelId(1L);
        hotel.setHotelName("Grand Hotel");
        hotel.setCity("Ankara");
        hotel.setStars(5);
        hotel.setPricePerNight(300.0);
        hotel.setAvailableRooms(20);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setHotel(hotel);
        booking.setTotalPrice(flight.getPrice() + hotel.getPricePerNight());
        booking.setStatus(BookingStatus.CREATED);
        booking.setBookingDate(LocalDateTime.now());

        requestDto = new BookingRequestDto(1L, 1L, 1L);
    }

    @Nested
    @DisplayName("createBooking()")
    class CreateBookingTests {

        @Test
        @DisplayName("Başarılı rezervasyon oluşturma")
        void createBooking_success() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
            when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
            when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

            BookingResponseDto response = bookingServiceImpl.createBooking(requestDto);

            assertThat(response).isNotNull();
            assertThat(response.getUserId()).isEqualTo(1L);
            assertThat(response.getFlightId()).isEqualTo(1L);
            assertThat(response.getHotelId()).isEqualTo(1L);
            assertThat(response.getTotalPrice()).isEqualTo(flight.getPrice() + hotel.getPricePerNight());
            assertThat(response.getStatus()).isEqualTo(BookingStatus.CREATED.name());

            verify(bookingRepository, times(1)).save(any(Booking.class));
        }

        @Test
        @DisplayName("Kullanıcı bulunamadığında ResourceNotFoundException fırlatır")
        void createBooking_userNotFound_throwsException() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingServiceImpl.createBooking(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found");

            verify(bookingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Uçuş bulunamadığında ResourceNotFoundException fırlatır")
        void createBooking_flightNotFound_throwsException() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(flightRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingServiceImpl.createBooking(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Flight not found");

            verify(bookingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Otel bulunamadığında ResourceNotFoundException fırlatır")
        void createBooking_hotelNotFound_throwsException() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
            when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingServiceImpl.createBooking(requestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Hotel not found");

            verify(bookingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Toplam fiyat uçuş + otel fiyatı olarak hesaplanır")
        void createBooking_totalPrice_calculatedCorrectly() {
            flight.setPrice(200.0);
            hotel.setPricePerNight(150.0);
            booking.setTotalPrice(350.0);

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
            when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
            when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

            BookingResponseDto response = bookingServiceImpl.createBooking(requestDto);

            assertThat(response.getTotalPrice()).isEqualTo(350.0);
        }
    }

    @Nested
    @DisplayName("getById()")
    class GetByIdTests {

        @Test
        @DisplayName("Mevcut rezervasyonu getirir")
        void getById_success() {
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            BookingResponseDto response = bookingServiceImpl.getById(1L);

            assertThat(response).isNotNull();
            assertThat(response.getBookingId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Rezervasyon bulunamadığında ResourceNotFoundException fırlatır")
        void getById_notFound_throwsException() {
            when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingServiceImpl.getById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Booking not found");
        }
    }

    @Nested
    @DisplayName("getAllBookings()")
    class GetAllBookingsTests {

        @Test
        @DisplayName("Tüm rezervasyonları liste olarak döner")
        void getAllBookings_success() {
            Booking booking2 = new Booking();
            booking2.setBookingId(2L);
            booking2.setUser(user);
            booking2.setFlight(flight);
            booking2.setHotel(hotel);
            booking2.setTotalPrice(800.0);
            booking2.setStatus(BookingStatus.CONFIRMED);
            booking2.setBookingDate(LocalDateTime.now());

            when(bookingRepository.findAll()).thenReturn(List.of(booking, booking2));

            List<BookingResponseDto> results = bookingServiceImpl.getAllBookings();

            assertThat(results).hasSize(2);
            verify(bookingRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Hiç rezervasyon yokken boş liste döner")
        void getAllBookings_empty() {
            when(bookingRepository.findAll()).thenReturn(List.of());

            List<BookingResponseDto> results = bookingServiceImpl.getAllBookings();

            assertThat(results).isEmpty();
        }
    }

    // =====================================================================
    // confirmBooking
    // =====================================================================
    @Nested
    @DisplayName("confirmBooking()")
    class ConfirmBookingTests {

        @Test
        @DisplayName("CREATED rezervasyonu başarıyla CONFIRMED'a çevirir")
        void confirmBooking_success() {
            booking.setStatus(BookingStatus.CREATED);
            Booking confirmedBooking = new Booking();
            confirmedBooking.setBookingId(1L);
            confirmedBooking.setUser(user);
            confirmedBooking.setFlight(flight);
            confirmedBooking.setHotel(hotel);
            confirmedBooking.setTotalPrice(800.0);
            confirmedBooking.setStatus(BookingStatus.CONFIRMED);
            confirmedBooking.setBookingDate(LocalDateTime.now());

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            when(flightRepository.save(any(Flight.class))).thenReturn(flight);
            when(bookingRepository.save(any(Booking.class))).thenReturn(confirmedBooking);

            BookingResponseDto response = bookingServiceImpl.confirmBooking(1L);

            assertThat(response.getStatus()).isEqualTo(BookingStatus.CONFIRMED.name());
            // Koltuk sayısı 10'dan 9'a inmeli
            assertThat(flight.getAvailableSeats()).isEqualTo(9);
        }

        @Test
        @DisplayName("CREATED dışındaki durumda BusinessException fırlatır")
        void confirmBooking_notCreatedStatus_throwsException() {
            booking.setStatus(BookingStatus.CONFIRMED);
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            assertThatThrownBy(() -> bookingServiceImpl.confirmBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only CREATED bookings can be confirmed");
        }

        @Test
        @DisplayName("Müsait koltuk yokken BusinessException fırlatır")
        void confirmBooking_noAvailableSeats_throwsException() {
            booking.setStatus(BookingStatus.CREATED);
            flight.setAvailableSeats(0);
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            assertThatThrownBy(() -> bookingServiceImpl.confirmBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("No available seats for this flight");
        }

        @Test
        @DisplayName("Rezervasyon bulunamadığında ResourceNotFoundException fırlatır")
        void confirmBooking_notFound_throwsException() {
            when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingServiceImpl.confirmBooking(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Booking not found");
        }
    }

    // =====================================================================
    // cancelBooking
    // =====================================================================
    @Nested
    @DisplayName("cancelBooking()")
    class CancelBookingTests {

        @Test
        @DisplayName("CONFIRMED rezervasyonu başarıyla CANCELLED'a çevirir")
        void cancelBooking_success() {
            booking.setStatus(BookingStatus.CONFIRMED);
            Booking cancelledBooking = new Booking();
            cancelledBooking.setBookingId(1L);
            cancelledBooking.setUser(user);
            cancelledBooking.setFlight(flight);
            cancelledBooking.setHotel(hotel);
            cancelledBooking.setTotalPrice(800.0);
            cancelledBooking.setStatus(BookingStatus.CANCELLED);
            cancelledBooking.setBookingDate(LocalDateTime.now());

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
            when(flightRepository.save(any(Flight.class))).thenReturn(flight);
            when(bookingRepository.save(any(Booking.class))).thenReturn(cancelledBooking);

            BookingResponseDto response = bookingServiceImpl.cancelBooking(1L);

            assertThat(response.getStatus()).isEqualTo(BookingStatus.CANCELLED.name());
            // İptal sonrası koltuk geri eklenmeli: 10 + 1 = 11
            assertThat(flight.getAvailableSeats()).isEqualTo(11);
        }

        @Test
        @DisplayName("CONFIRMED dışındaki durumda BusinessException fırlatır")
        void cancelBooking_notConfirmedStatus_throwsException() {
            booking.setStatus(BookingStatus.CREATED);
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            assertThatThrownBy(() -> bookingServiceImpl.cancelBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Only CONFIRMED bookings can be cancelled");
        }

        @Test
        @DisplayName("Rezervasyon bulunamadığında ResourceNotFoundException fırlatır")
        void cancelBooking_notFound_throwsException() {
            when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookingServiceImpl.cancelBooking(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Booking not found");
        }
    }
}
