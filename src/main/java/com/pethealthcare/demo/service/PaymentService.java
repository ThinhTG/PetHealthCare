package com.pethealthcare.demo.service;


import com.pethealthcare.demo.dto.request.BookingCancelRequest;

import com.pethealthcare.demo.config.VNPayConfig;

import com.pethealthcare.demo.dto.request.PaymentCreateRequest;
import com.pethealthcare.demo.mapper.PaymentMapper;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.Payment;
import com.pethealthcare.demo.responsitory.BookingRepository;
import com.pethealthcare.demo.responsitory.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;


    public Payment createPayment(PaymentCreateRequest request) {
        if (request.getDeposit() < request.getTotal() * 0.3) {
            throw new IllegalArgumentException("Deposit must be at least 30% of total");

    @Autowired
    private VNPayConfig vnPayConfig;

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();

        }
        return ipAdress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
    public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeKey) {
        return paramsMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry ->
                        (encodeKey ? URLEncoder.encode(entry.getKey(),
                                StandardCharsets.US_ASCII)
                                : entry.getKey()) + "=" +
                                URLEncoder.encode(entry.getValue()
                                        , StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }

    public String createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        int bookingId = Integer.parseInt(request.getParameter("bookingId"));
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_OrderInfo", "" + bookingId);
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", getIpAddress(request));
        String queryUrl = getPaymentURL(vnpParamsMap, true);
        String hashData = getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return paymentUrl;
    }

    public Payment createPayment(int transactionNo, int amount, String bankCode,
                                 String bankTranNo, String cardType, String vnpPayDate, String orderInfo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime payDate = LocalDateTime.parse(vnpPayDate, formatter);
        int bookingId = Integer.parseInt(orderInfo);
        Payment payment = new Payment();
        payment.setTransactionNo(transactionNo);
        payment.setAmount(amount/100);
        payment.setBankCode(bankCode);
        payment.setBankTranNo(bankTranNo);
        payment.setCardType(cardType);
        payment.setPayDate(payDate);
        payment.setBooking(bookingRepository.findBookingByBookingId(bookingId));
        bookingService.updateStatusBooking(bookingId, "PAID");
        return paymentRepository.save(payment);
    }

    public double getRevenue() {
        double revunue = 0.0;
        for (Payment payment : paymentRepository.findAll()) {
            revunue += payment.getTotal();
        }
        return revunue;
    }

    public double getRevenueInPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findAll();
        double revenue = 0.0;
        for (Payment payment : payments) {
            if (payment.getPaymentDate().isAfter(startDate) && payment.getPaymentDate().isBefore(endDate)) {
                revenue += payment.getTotal();
            }
        }
        return revenue;
    }

    public double returnDeposit(int bookingId, BookingCancelRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId()).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        Payment payment = paymentRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        LocalDateTime paymentDate = payment.getPaymentDate();
        LocalDateTime sevenDaysAfterPayment = paymentDate.plusDays(7);

            if (request.getPaymentDate().isBefore(sevenDaysAfterPayment)) {
                payment.setDeposit(payment.getDeposit()*0.5);
            }

        return payment.getDeposit();
    }
    }

