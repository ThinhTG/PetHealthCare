package com.pethealthcare.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "[User]")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String name;
    @Column(columnDefinition = "nvarchar(50)", nullable = false, unique = true)
    private String email;
    @Column(columnDefinition = "nvarchar(70)", nullable = false)
    private String password;
    @Column(columnDefinition = "decimal(10,0)", nullable = false)
    private BigDecimal phone;
    @Column(columnDefinition = "nvarchar(50)")
    private String address;
    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String role;
    @Column(columnDefinition = "nvarchar(50)", nullable = false)
    private String status;
}
