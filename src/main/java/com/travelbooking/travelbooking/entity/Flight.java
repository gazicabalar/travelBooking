package com.travelbooking.travelbooking.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String fromCity;

    @Column(nullable = false)
    @NotBlank
    private String toCity;

    @Column(nullable = false)
    @NotNull(message = "Flight date cannot be blank")
    private LocalDate flightDate;

    @Column(nullable = false)
    @NotNull(message = "Price cannot be blank")
    private Double price;

    @Column(nullable = false)
    @NotNull(message = "Available seats cannot be blank")
    private int availableSeats;

}
