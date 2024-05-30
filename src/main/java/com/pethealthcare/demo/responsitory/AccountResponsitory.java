package com.pethealthcare.demo.responsitory;

import com.pethealthcare.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountResponsitory extends JpaRepository<Account,String> {
   // List<Account> findAccountByEmail(String email);
}
