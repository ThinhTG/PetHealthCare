package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedbackId;

    @Column
    private String feedbackContent;

    @Column
    private int rating;

    @JsonIgnore
    @OneToOne(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookingDetail bookingDetail;
}
