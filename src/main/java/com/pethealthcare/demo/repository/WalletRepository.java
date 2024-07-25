package com.pethealthcare.demo.repository;

import com.pethealthcare.demo.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
    Wallet findWalletByUser_UserId(int userId);
    Wallet findWalletByWalletId(int walletId);
}
