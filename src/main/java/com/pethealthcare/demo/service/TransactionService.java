package com.pethealthcare.demo.service;


import com.pethealthcare.demo.config.VNPayConfig;
import com.pethealthcare.demo.dto.request.TransactionCreateRequest;
import com.pethealthcare.demo.enums.TransactionType;
import com.pethealthcare.demo.model.Booking;
import com.pethealthcare.demo.model.Transaction;
import com.pethealthcare.demo.model.Wallet;
import com.pethealthcare.demo.repository.BookingRepository;
import com.pethealthcare.demo.repository.TransactionRepository;
import com.pethealthcare.demo.repository.WalletRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VNPayConfig vnPayConfig;

    @Autowired
    private WalletRepository walletRepository;

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
        int walletId = Integer.parseInt(request.getParameter("walletId"));
        Map<String, String> vnpParamsMap = vnPayConfig.getPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_OrderInfo", "" + walletId);
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", getIpAddress(request));
        String queryUrl = getPaymentURL(vnpParamsMap, true);
        String hashData = getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }

    public Transaction deposit(int amount, String vnpPayDate, String orderInfo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime payDate = LocalDateTime.parse(vnpPayDate, formatter);
        int walletId = Integer.parseInt(orderInfo);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount / 100);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionDate(payDate);
        Wallet wallet = walletRepository.findWalletByWalletId(walletId);
        wallet.setBalance(wallet.getBalance() + (double) amount / 100);
        walletRepository.save(wallet);
        transaction.setWallet(wallet);
        return transactionRepository.save(transaction);
    }

    public String payBooking(TransactionCreateRequest request){
        Wallet wallet = walletRepository.findWalletByWalletId(request.getWalletId());
        if(wallet.getBalance() < request.getAmount()){
            return "Account balance is not enough to make transactions";
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setTransactionDate(LocalDateTime.now());
        wallet.setBalance(wallet.getBalance() - request.getAmount());
        walletRepository.save(wallet);
        transaction.setWallet(wallet);
        Booking booking = bookingRepository.findBookingByBookingId(request.getBookingId());
        booking.setStatus("PAID");
        transaction.setBooking(booking);
        transactionRepository.save(transaction);
        return "Payment success";
    }

    public List<Transaction> getTransactionsByWalletId(int walletId){
        return transactionRepository.findTransactionsByWallet_WalletId(walletId);
    }
}

