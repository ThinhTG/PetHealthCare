package com.pethealthcare.demo.controller;

import com.pethealthcare.demo.dto.request.FeedbackRequest;
import com.pethealthcare.demo.model.Feedback;
import com.pethealthcare.demo.response.ResponseObject;
import com.pethealthcare.demo.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create/{id}")
    ResponseEntity<ResponseObject> createFeedBack(@PathVariable int id, @RequestBody FeedbackRequest request) {
        Feedback feedback = feedbackService.createFeedback(id, request);
        if (feedback != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("ok", "Feedback has been accepted", feedback)
            );
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Try again", "")
            );
        }
    }

    @GetMapping("/get/{id}")
    ResponseEntity<ResponseObject> getFeedbackByBookingDetailId(@PathVariable int id) {
        Feedback feedback = feedbackService.getFeedbackByBookingDetailId(id);
        if (feedback != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Feedback found", feedback)
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Feedback not found", "")
            );
        }
    }

}
