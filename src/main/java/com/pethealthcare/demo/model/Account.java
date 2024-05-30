package com.pethealthcare.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Account")
public class Account {
    @Id
    @Column(name = "Email", columnDefinition = "nvarchar(50)")
    private String Email;
    @Column(name = "Password", columnDefinition = "nvarchar(50)")
    private String Password;
    @Column(name = "Role", columnDefinition = "nvarchar(50)")
    private String Role;
}
