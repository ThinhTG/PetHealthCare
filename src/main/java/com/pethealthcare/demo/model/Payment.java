package com.pethealthcare.demo.model;

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
    private int transactionNo;

    @Column
    private int amount;

    @Column
    private String bankCode;

    @Column
    private String bankTranNo;

    @Column
    private String CardType;

    @Column
    private LocalDateTime payDate;

    @Column
    private int txnRef;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;

}
