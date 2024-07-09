package com.pethealthcare.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cage")
@Entity

public class Cage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cageId;

    @Column
    private boolean status;

    @OneToOne
    @JoinColumn(name = "petId")
    private Pet pet;

    @Column
    @Enumerated(EnumType.STRING)
    private CageType cageType;
}
