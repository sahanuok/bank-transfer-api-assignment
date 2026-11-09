package com.example.bankapp.service;

import com.example.bankapp.model.BankTransaction;
import com.example.bankapp.model.TransactionStatus;
import com.example.bankapp.repository.BankTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionAuditService {
    
    private final BankTransactionRepository repository;

    // මෙතන REQUIRES_NEW දැම්මම ප්‍රධාන Transaction එක fail වුණත් මේක වෙනම සේව් වෙනවා
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(String from, String to, BigDecimal amount, String error) {
        BankTransaction transaction = new BankTransaction();
        transaction.setFromAccount(from);
        transaction.setToAccount(to);
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setDescription(error);
        repository.save(transaction);
    }
}