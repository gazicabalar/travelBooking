package com.travelbooking.travelbooking.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.travelbooking.travelbooking.repository.FlightRepository;
import com.travelbooking.travelbooking.entity.Flight;
import com.travelbooking.travelbooking.dto.FlightRequestDto;
import com.travelbooking.travelbooking.dto.FlightResponseDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlightServiceImpl Testleri")
public class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    private Flight flight;
    private FlightRequestDto flightRequestDto;

    @BeforeEach
    void setUp() {
        flight = Flight.builder()
                .flightId(1L)
                .fromCity("İstanbul")
                .toCity("Balıkesir")
                .price(4000.0)
                .flightDate(LocalDate.now().plusDays(3))
                .availableSeats(150)
                .build();

        flightRequestDto = new FlightRequestDto();
        flightRequestDto.setFromCity("Istanbul");
        flightRequestDto.setToCity("Balıkesir");
        flightRequestDto.setFlightDate(LocalDate.now().plusDays(3));
        flightRequestDto.setPrice(4000.0);
    }

    @Test
    @DisplayName("Uçuş başarıyla oluşturulmalı")
    void shouldCreateFlightSuccessfully() {

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        FlightResponseDto result = flightService.createFlight(flightRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getFlightId()).isEqualTo(1L);
        assertThat(result.getFromCity()).isEqualTo("İstanbul");
        assertThat(result.getToCity()).isEqualTo("Balıkesir");
        assertThat(result.getPrice()).isEqualTo(4000.0);
        assertThat(result.getAvailableSeats()).isEqualTo(150);

        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    @DisplayName("ID ile uçuş başarıyla alınmalı")
    void shouldGetFlightByIdSuccessfully(){

        when(flightRepository.findById(1L)).thenReturn(Optional.ofNullable(flight));

        FlightResponseDto result = flightService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFlightId()).isEqualTo(1L);
        assertThat(result.getFromCity()).isEqualTo(flight.getFromCity());
        assertThat(result.getToCity()).isEqualTo(flight.getToCity());
        assertThat(result.getPrice()).isEqualTo(flight.getPrice());
        assertThat(result.getPrice()).isEqualTo(flight.getPrice());

        verify(flightRepository,times(1)).findById(1L);
    }

    @Test
    @DisplayName("Tüm uçuşlar başarıyla alınmalı")
    void shouldGetAllFlightsSuccessfully(){

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<FlightResponseDto> result = flightService.getAllFlights();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(flightRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Uçuş başarıyla silinmeli")
    void shouldDeleteFlightSuccessfully() {
        when(flightRepository.findById(1L)).thenReturn(Optional.ofNullable(flight));

        String result = flightService.deleteFlight(1L);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("Flight deleted successfully");
        verify(flightRepository, times(1)).findById(1L);
        verify(flightRepository, times(1)).delete(any(Flight.class));

    }

    @Test
    @DisplayName("Uçuş bulunamadığında istisna fırlatılmalı")
    void shouldThrowExceptionWhenFlightNotFound(){

        when(flightRepository.findById(15L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> flightService.getById(15L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Flight not found");
        verify(flightRepository, times(1)).findById(15L);
    }

}
