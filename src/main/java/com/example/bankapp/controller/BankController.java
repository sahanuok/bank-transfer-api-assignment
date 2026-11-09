package com.example.bankapp.controller;

import com.example.bankapp.dto.TransferRequest;
import com.example.bankapp.model.Customer;
import com.example.bankapp.model.BankTransaction;
import com.example.bankapp.service.BankService;
import com.example.bankapp.repository.CustomerRepository;
import com.example.bankapp.repository.BankTransactionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;
    private final CustomerRepository customerRepository;
    private final BankTransactionRepository transactionRepository;

    // 1. ගිණුම්වල විස්තර බැලීම (GET)
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // 2. සල්ලි යැවීම (POST)
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@Valid @RequestBody TransferRequest request) {
        try {
            bankService.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount(), request.isSimulateFailure());
            return ResponseEntity.ok("Transfer successful!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Transfer failed: " + e.getMessage());
        }
    }

    // 3. History බැලීම (GET)
    @GetMapping("/history/{accountNumber}")
    public List<BankTransaction> getHistory(@PathVariable String accountNumber) {
        return transactionRepository.findHistory(accountNumber);
    }
}