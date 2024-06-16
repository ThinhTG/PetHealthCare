package com.pethealthcare.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Serivce")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;

    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String name;

    @Column(columnDefinition = "money", nullable = false)
    private double price;

    @Column(columnDefinition = "text", nullable = false)
    private String description;

    @Column(columnDefinition = "nvarchar(255)")
    private String imageUrl;

    @OneToMany(mappedBy = "services", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetail> bookingDetails;
}
