package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pethealthcare.demo.enums.BookingDetailStatus;
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
@ToString
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingDetailId;

    @Column(columnDefinition = "bit")
    private boolean needCage;

    @Column(columnDefinition = "date")
    private LocalDate date;

    @Column
    @Enumerated(EnumType.STRING)
    private BookingDetailStatus status;

    // vetCancelled is a boolean field that is used to determine if the vet has cancelled the booking
    // true la vet da huy booking, false la vet chua huy booking
    @Column
    private boolean vetCancelled;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "petId")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "serviceId")
    private Services services;

    @ManyToOne
    @JoinColumn(name = "slotId")
    private Slot slot;


    @JsonIgnore
    @OneToOne(mappedBy = "bookingDetail", cascade = CascadeType.ALL)
    private Feedback feedback;

    @JsonIgnore
    @OneToOne(mappedBy = "bookingDetail", cascade = CascadeType.ALL)
    private Refund refund;
}
