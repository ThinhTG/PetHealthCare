package com.pethealthcare.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "[Transaction]")
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    private int amount;

    private String transactionType;

    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "walletId")
    private Wallet wallet;
}
