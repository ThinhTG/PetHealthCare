package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.Transaction;
import com.pethealthcare.demo.response.RevenueResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findTransactionByBooking_BookingId(int bookingId);
    List<Transaction> findTransactionsByWallet_WalletId(int walletId);

    @Query("SELECT new com.pethealthcare.demo.response.RevenueResponse(MONTH(t.transactionDate), " +
            "SUM(CASE WHEN t.transactionType = 'PAYMENT' THEN t.amount " +
            "WHEN t.transactionType = 'REFUND' THEN -t.amount ELSE 0 END)) " +
            "FROM Transaction t " +
            "WHERE YEAR(t.transactionDate) = :year " +
            "GROUP BY MONTH(t.transactionDate)")
    List<RevenueResponse> calculateMonthlyRevenue(@Param("year") int year);
}
