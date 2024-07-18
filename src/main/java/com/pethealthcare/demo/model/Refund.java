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

    @Column
    private int amount;

    @Column
    private int refundPercent;

    @Column
    private LocalDate refundDate;

//    @JsonIgnore
//    @OneToOne
//    @JoinColumn(name = "bookingId")
//    private Booking booking;


    @OneToOne
    @JoinColumn(name = "bookingDetailId")
    private BookingDetail bookingDetail;
}
