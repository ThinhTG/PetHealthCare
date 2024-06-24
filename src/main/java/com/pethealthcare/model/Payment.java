package com.pethealthcare.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Payment")
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    @Column
    private String paymentMethod;

    @Column
    private LocalDateTime paymentDate;

    @Column
    private double deposit;

    @Column
    private double total;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;

}
