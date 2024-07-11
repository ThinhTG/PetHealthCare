package com.pethealthcare.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int feedbackId;

    @Column(name = "FeedbackContent",columnDefinition = "nvarchar(50)")
    private String feedbackContent;

    @Column(name = "Age")
    private int rating;

    @JsonIgnore
    @OneToOne(mappedBy = "feedBack", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookingDetail bookingDetail;


}
