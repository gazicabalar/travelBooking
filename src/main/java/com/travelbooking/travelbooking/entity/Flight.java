package com.travelbooking.travelbooking.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private LocalDate flightDate;

    @Column(nullable = false)
    @NotBlank
    private Double price;

    @Column(nullable = false)
    @NotBlank
    private int availableSeats;

}
