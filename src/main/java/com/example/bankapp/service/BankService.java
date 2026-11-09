package com.example.bankapp.service;

import com.example.bankapp.exception.AccountNotFoundException;
import com.example.bankapp.exception.InsufficientBalanceException;
import com.example.bankapp.model.BankTransaction;
import com.example.bankapp.model.TransactionStatus;
import com.example.bankapp.repository.BankTransactionRepository;
import com.example.bankapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankService {
    
    private final CustomerRepository customerRepository;
    private final BankTransactionRepository transactionRepository;
    private final TransactionAuditService auditService;

    @Transactional(rollbackFor = Exception.class)
    public BankTransaction transfer(String fromAccount, String toAccount, BigDecimal amount, boolean simulateFailure) {
        
        if (fromAccount.equals(toAccount)) {
            throw new IllegalArgumentException("Sender and receiver must be different accounts.");
        }

        // 1. ගිණුම් තියෙනවද කියලා බලනවා
        if (!customerRepository.countAccounts(fromAccount)) {
            throw new AccountNotFoundException("Sender account not found.");
        }
        if (!customerRepository.countAccounts(toAccount)) {
            throw new AccountNotFoundException("Receiver account not found.");
        }

        // 2. යවන කෙනාගෙන් සල්ලි අඩු කරනවා
        int updatedRows = customerRepository.debit(fromAccount, amount);
        if (updatedRows == 0) {
            throw new InsufficientBalanceException("Insufficient balance in account " + fromAccount);
        }

        // 3. හිතාමතාම එරර් එකක් හදනවා (Rollback වෙනවද බලන්න)
        if (simulateFailure) {
            throw new RuntimeException("Simulated system failure after the withdrawal.");
        }

        // 4. ලබන කෙනාට සල්ලි එකතු කරනවා
        customerRepository.credit(toAccount, amount);

        // 5. සාර්ථක Transaction එක සේව් කරනවා
        BankTransaction transaction = new BankTransaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription("Transfer completed");
        
        return transactionRepository.save(transaction);
    }
}