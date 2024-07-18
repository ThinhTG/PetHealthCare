package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Refund")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionNo;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
        private int refundPercent;

    @Column(nullable = false)
    private LocalDate payDate;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "bookingId", nullable = false)
    private Booking booking;
}
