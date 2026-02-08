package com.travelbooking.travelbooking.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @Column(nullable = false)
    private String fromCity;

    @Column(nullable = false)
    private String toCity;

    @Column(nullable = false)
    @Future(message = "Flight date must be in the future")
    private LocalDate flightDate;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int availableSeats;

}
