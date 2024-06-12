//package com.pethealthcare.demo.model;
//
//
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Entity
//@Table(name = "[Booking]")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class Booking {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "bookingID")
//    private int bookingID;
//
//    @Column(columnDefinition = "nvarchar(50)", nullable = false)
//    @OneToOne
//    @JoinColumn(name = "userID",nullable = false,referencedColumnName = "userID")
//    private User user;
//
//    @Column(columnDefinition = "datetime", nullable = false)
//    private LocalDateTime time;
//
//    @Column(columnDefinition = "nvarchar(50)", nullable = false)
//    private String status;
//
//    @Column(columnDefinition = "money", nullable = false)
//    private float totalPrice;
//
//    @OneToMany(mappedBy = "Booking",cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private Set<BookingDetail> bookingDetails;
//}
