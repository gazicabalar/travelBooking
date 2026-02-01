package com.travelbooking.travelbooking.repository;

import com.travelbooking.travelbooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByCity(String city);
    List<Hotel> findByCityAndStars(String city, int stars);

}
