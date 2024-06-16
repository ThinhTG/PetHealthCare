package com.pethealthcare.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Date date;

    @Column
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "slotId")
    private Slot slot;
}
