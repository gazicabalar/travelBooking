package com.travelbooking.travelbooking.repository;

import com.travelbooking.travelbooking.entity.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FlightRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FlightRepository flightRepository;

    private Flight flight1;
    private Flight flight2;

    @BeforeEach
    void setUp() {
        flight1 = Flight.builder()
                .fromCity("Istanbul")
                .toCity("Ankara")
                .flightDate(LocalDate.now().plusDays(1))
                .price(500.0)
                .availableSeats(100)
                .build();

        flight2 = Flight.builder()
                .fromCity("Istanbul")
                .toCity("Izmir")
                .flightDate(LocalDate.now().plusDays(3))
                .price(300.0)
                .availableSeats(50)
                .build();
    }

    @Test
    @DisplayName("Uçuş kaydedildiğinde ID atanmalı ve geri alınabilmeli")
    void givenFlight_whenSave_thenFlightIsPersisted() {

        Flight savedFlight = flightRepository.save(flight1);

        assertThat(savedFlight.getFlightId()).isNotNull();
        assertThat(savedFlight.getFromCity()).isEqualTo("Istanbul");
        assertThat(savedFlight.getToCity()).isEqualTo("Ankara");
        assertThat(savedFlight.getPrice()).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Var olan bir uçuş ID ile bulunabilmeli")
    void givenPersistedFlight_whenFindById_thenReturnFlight() {

        Flight persisted = entityManager.persistAndFlush(flight1);

        Optional<Flight> found = flightRepository.findById(persisted.getFlightId());

        assertThat(found).isPresent();
        assertThat(found.get().getFromCity()).isEqualTo("Istanbul");
    }

    @Test
    @DisplayName("Tüm uçuşlar listelenebilmeli")
    void givenTwoFlights_whenFindAll_thenReturnBoth() {

        entityManager.persistAndFlush(flight1);
        entityManager.persistAndFlush(flight2);

        List<Flight> flights = flightRepository.findAll();

        assertThat(flights).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Şehir ve tarihe göre uçuşlar bulunabilmeli")
    void givenFlight_whenFindByFromCityAndToCityAndFlightDate_thenReturnMatchingFlights() {

        entityManager.persistAndFlush(flight1);
        entityManager.persistAndFlush(flight2);

        List<Flight> result = flightRepository.findByFromCityAndToCityAndFlightDate(
                "Istanbul",
                "Ankara",
                LocalDate.now().plusDays(1)
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getToCity()).isEqualTo("Ankara");
    }

    @Test
    @DisplayName("Eşleşmeyen kriterlerde boş liste dönmeli")
    void givenNoMatchingFlight_whenFindByFromCityAndToCityAndFlightDate_thenReturnEmpty() {

        entityManager.persistAndFlush(flight1);

        List<Flight> result = flightRepository.findByFromCityAndToCityAndFlightDate(
                "Ankara",
                "Berlin",
                LocalDate.now().plusDays(1)
        );

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Silinen uçuş artık bulunamıyor olmalı")
    void givenPersistedFlight_whenDelete_thenFlightIsRemoved() {

        Flight persisted = entityManager.persistAndFlush(flight1);
        Long id = persisted.getFlightId();

        flightRepository.deleteById(id);

        Optional<Flight> deleted = flightRepository.findById(id);
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("Uçuş güncellendikten sonra yeni değer okunabilmeli")
    void givenPersistedFlight_whenUpdatePrice_thenNewPriceIsSaved() {

        Flight persisted = entityManager.persistAndFlush(flight1);

        persisted.setPrice(999.0);
        flightRepository.save(persisted);

        entityManager.flush();
        entityManager.clear();

        Optional<Flight> updated = flightRepository.findById(persisted.getFlightId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getPrice()).isEqualTo(999.0);
    }
}
