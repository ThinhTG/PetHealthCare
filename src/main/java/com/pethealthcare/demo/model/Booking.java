package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pethealthcare.demo.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate date;

    @Column
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column
    private double totalPrice;

    @JsonIgnore
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetail> bookingDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transaction;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

}
