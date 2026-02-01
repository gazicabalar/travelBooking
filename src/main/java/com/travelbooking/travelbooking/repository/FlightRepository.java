package com.travelbooking.travelbooking.repository;

import com.travelbooking.travelbooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByFromCityAndToCityAndFlightDate(String fromCity, String toCity, LocalDate flightDate);

}
