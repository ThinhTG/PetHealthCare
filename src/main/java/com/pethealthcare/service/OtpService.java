package com.pethealthcare.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public String generateOtp(String email) {
        String otp = String.format("%06d", random.nextInt(999999));
        otpStorage.put(email, otp);

        scheduler.schedule(() -> clearOtp(email), 5, TimeUnit.MINUTES);

        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }
}

