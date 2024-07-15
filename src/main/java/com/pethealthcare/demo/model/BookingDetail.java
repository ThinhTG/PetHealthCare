package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "BookingDetail")
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingDetailId;

    @Column(columnDefinition = "bit")
    private boolean needCage;

    @Column(columnDefinition = "date")
    private LocalDate date;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "petId")
    private Pet pet;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "serviceId")
    private Services services;

    @ManyToOne
    @JoinColumn(name = "slotId")
    private Slot slot;

    @OneToOne
    @JoinColumn(name = "feedbackId")
    private Feedback feedback;
}