package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pethealthcare.demo.enums.ServiceSlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "ServiceSlot")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceSlotId;

    @Column
    private LocalDate date;

    @Column
    @Enumerated(EnumType.STRING)
    private ServiceSlotStatus status;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "slotId")
    private Slot slot;
}
