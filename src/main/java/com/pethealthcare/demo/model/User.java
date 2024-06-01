package com.pethealthcare.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "[User]")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private int Userid ;
    @Column(name = "Name", columnDefinition = "nvarchar(50)")
    private String Name;
    @Column(name = "Email", columnDefinition = "nvarchar(50)")
    private String Email;
    @Column(name = "Password",columnDefinition = "nvarchar(50)")
    private String Password;
    @Column(name = "Phone", columnDefinition = "nvarchar(50)")
    private String Phone;
    @Column(name = "Address",columnDefinition = "nvarchar(50)")
    private String Address;
    @Column(name = "Role",columnDefinition = "nvarchar(50)")
    private String Role;
    @Column(name = "Status",columnDefinition = "nvarchar(50)")
    private String Status;
}
