package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "[User]")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String name;

    @Column(columnDefinition = "nvarchar(50)", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(columnDefinition = "nvarchar(70)", nullable = false)
    private String password;

    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String phone;

    @Column(columnDefinition = "nvarchar(50)")
    private String address;

    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String role;

    @Column
    private boolean status;

    @Column(columnDefinition = "nvarchar(MAX)")
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetail> bookingDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ServiceSlot> serviceSlots;

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;
}
