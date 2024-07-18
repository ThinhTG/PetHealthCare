package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.time.LocalDate;


import java.time.LocalDateTime;

import java.util.Date;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingId;

    @Column
    private Date date;

    @Column
    private String status;

    @Column
    private double totalPrice;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetail> bookingDetails;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payment;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;


    @JsonIgnore
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Refund refund;
}

}
