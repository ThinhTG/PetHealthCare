package com.pethealthcare.demo.service;

import com.pethealthcare.demo.dto.request.FeedbackRequest;
import com.pethealthcare.demo.model.BookingDetail;
import com.pethealthcare.demo.model.Feedback;
import com.pethealthcare.demo.responsitory.BookingDetailRepository;
import com.pethealthcare.demo.responsitory.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private BookingDetailRepository bookingDetailRepository;

    public Feedback createFeedback(int bookingDetailId, FeedbackRequest feedbackRequest){
        BookingDetail bookingDetail = bookingDetailRepository.findBookingDetailByBookingDetailId(bookingDetailId);

        Feedback feedback = new Feedback();
        feedback.setBookingDetail(bookingDetail);
        feedback.setFeedbackContent(feedbackRequest.getFeedbackContent());
        feedback.setRating(feedbackRequest.getRating());

        return feedbackRepository.save(feedback);
    }

}
