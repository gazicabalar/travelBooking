package com.travelbooking.travelbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private Long bookingId;
    private Long userId;
    private Long flightId;
    private Long hotelId;
    private Double totalPrice;
    private String status;
    private LocalDateTime bookingDate;
}
