package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findTransactionByBooking_BookingId(int bookingId);
    List<Transaction> findTransactionsByWallet_WalletId(int walletId);
}
