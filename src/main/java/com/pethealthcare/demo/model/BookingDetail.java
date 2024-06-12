package com.pethealthcare.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

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

//    @Column
//    private int bookingId;

    @Column
    private int petId;

    @Column(name = "UserID")
    private int veterinarianId;

    @Column
    private int serviceId;

    @Column(columnDefinition = "bit")
    private boolean needCage;

    @Column(columnDefinition = "date")
    private Date date;

    @Column
    private int slot;

    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;
}
