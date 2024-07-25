package com.pethealthcare.demo.service;

import com.pethealthcare.demo.model.User;
import com.pethealthcare.demo.model.Wallet;
import com.pethealthcare.demo.repository.UserRepository;
import com.pethealthcare.demo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    public void createWallet(int userId) {
        Wallet wallet = new Wallet();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        wallet.setUser(user);
        wallet.setBalance(0);
        walletRepository.save(wallet);
    }

    public Wallet getWallet(int userId) {
        return walletRepository.findWalletByUser_UserId(userId);
    }

}
