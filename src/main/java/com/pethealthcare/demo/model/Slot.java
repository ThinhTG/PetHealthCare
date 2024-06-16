package com.pethealthcare.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Slot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slotId;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
    private List<ServiceSlot> serviceSlots;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetail> bookingDetails;
}
