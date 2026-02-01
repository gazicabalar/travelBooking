package com.travelbooking.travelbooking.repository;

import com.travelbooking.travelbooking.entity.Booking;
import com.travelbooking.travelbooking.entity.BookingStatus;
import com.travelbooking.travelbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);
    List<Booking> findByStatus(BookingStatus status);

}
