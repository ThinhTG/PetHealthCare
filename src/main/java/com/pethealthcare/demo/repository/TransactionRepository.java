package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findTransactionByBooking_BookingId(int bookingId);
}
