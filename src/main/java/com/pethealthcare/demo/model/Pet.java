package com.pethealthcare.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "[Pet]")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petid;
    @Column(name = "UserID", columnDefinition = "nvarchar(50)")
    private int userID;
    @Column(name = "Name",columnDefinition = "nvarchar(50)")
    private String petname;
    @Column(name = "Age")
    private int petage;
    @Column(name = "Gender",columnDefinition = "nvarchar(50)")
    private String petgender;
    @Column(name = "Type",columnDefinition = "nvarchar(50)")
    private String pettype;
    @Column(name = "Vaccination",columnDefinition = "nvarchar(50)")
    private String vaccination;

}
