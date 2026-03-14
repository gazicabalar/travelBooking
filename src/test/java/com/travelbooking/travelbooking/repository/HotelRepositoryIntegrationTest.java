package com.travelbooking.travelbooking.repository;


import com.travelbooking.travelbooking.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HotelRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel hotel1;
    private Hotel hotel2;

    @BeforeEach
    void setUp(){
        hotel1 = Hotel.builder()
                .hotelName("Hilton")
                .city("Istanbul")
                .stars(7)
                .availableRooms(50)
                .pricePerNight(5000.0)
                .build();

        hotel2 = Hotel.builder()
                .hotelName("Ramada")
                .city("Ankara")
                .stars(5)
                .availableRooms(80)
                .pricePerNight(4000.0)
                .build();
    }

    @Test
    @DisplayName("Hotel kaydedildiğinde ID atanmalı ve geri alınabilmeli")
    void givenHotel_whenSave_thenReturnIsPersisted(){
        Hotel savedHotel = hotelRepository.save(hotel1);

        assertThat(savedHotel.getHotelId()).isNotNull();
        assertThat(savedHotel.getHotelName()).isEqualTo("Hilton");
        assertThat(savedHotel.getCity()).isEqualTo("Istanbul");
        assertThat(savedHotel.getPricePerNight()).isEqualTo(5000.0);
    }

    @Test
    @DisplayName("Var olan bir hotel ID ile bulunabilmeli")
    void givenPersistHotel_whenById_thenReturnHotel(){
        Hotel persistedHotel = testEntityManager.persistAndFlush(hotel1);

        Optional<Hotel> foundHotel = hotelRepository.findById(persistedHotel.getHotelId());

        assertThat(foundHotel.isPresent());
        assertThat(foundHotel.get().getHotelName()).isEqualTo("Hilton");
    }

    @Test
    @DisplayName("Birden fazla hotel kaydedildiğinde findAll() tüm hotel listesini döndürmeli")
    void givenTwoHotels_whenFindAll_thenReturnHotelList(){
        testEntityManager.persistAndFlush(hotel1);
        testEntityManager.persistAndFlush(hotel2);

        List<Hotel> hotels = hotelRepository.findAll();

        assertThat(hotels).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Belirli bir şehir ve yıldız sayısına göre hotel bulunabilmeli")
    void givenHotel_whenFindByFromCityAndStars_thenReturnHotel(){
        testEntityManager.persistAndFlush(hotel1);
        testEntityManager.persistAndFlush(hotel2);

        List<Hotel> result = hotelRepository.findByCityAndStars("Istanbul", 7);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHotelName()).isEqualTo("Hilton");
    }

    @Test
    @DisplayName("Belirli bir şehir ve yıldız sayısına göre eşleşen hotel bulunmazsa boş liste döndürmeli")
    void givenNoMatchingHotel_whenFindByFromCityAndStars_thenReturnHotel(){
        testEntityManager.persistAndFlush(hotel1);
        testEntityManager.persistAndFlush(hotel2);
        List<Hotel> result = hotelRepository.findByCityAndStars("İzmir", 7);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Silinen hotel artık bulunamıyor olmalı")
    void givenPersistedHotel_whenDelete_thenHotelIsDeleted(){
        Hotel persistedHotel = testEntityManager.persistAndFlush(hotel1);

        hotelRepository.deleteById(persistedHotel.getHotelId());

        Optional<Hotel> deletedHotel = hotelRepository.findById(persistedHotel.getHotelId());
        assertThat(deletedHotel).isNotPresent();
    }

    @Test
    @DisplayName("Hotel fiyatı güncellendiğinde yeni fiyat kaydedilmeli")
    void givenPersistedHotel_whenUpdatePricePerNight_thenUpdatePricePerNight(){
        Hotel persistedHotel = testEntityManager.persistAndFlush(hotel1);

        persistedHotel.setPricePerNight(5500.0);
        hotelRepository.save(persistedHotel);

        testEntityManager.flush();
        testEntityManager.clear();

        Optional<Hotel>  updatedHotel = hotelRepository.findById(persistedHotel.getHotelId());
        assertThat(updatedHotel.isPresent()).isTrue();
        assertThat(updatedHotel.get().getPricePerNight()).isEqualTo(5500.0);
    }

}
