package com.pethealthcare.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petId;

    @Column(name = "Name",columnDefinition = "nvarchar(50)")
    private String petName;

    @Column(name = "Age")
    private int petAge;

    @Column(name = "Gender",columnDefinition = "nvarchar(50)")
    private String petGender;

    @Column(name = "Type",columnDefinition = "nvarchar(50)")
    private String petType;

    @Column(name = "Vaccination",columnDefinition = "nvarchar(50)")
    private String vaccination;

    @JsonIgnore
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetail> bookingDetails;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
