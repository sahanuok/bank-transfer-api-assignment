package com.example.bankapp.repository;

import com.example.bankapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    @Query("SELECT c FROM Customer c WHERE c.accountNumber = :accountNumber")
    Optional<Customer> getByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.accountNumber = :accountNumber")
    boolean countAccounts(@Param("accountNumber") String accountNumber);

    @Query("SELECT c FROM Customer c ORDER BY c.fullName ASC")
    List<Customer> findAllOrdered();

    @Query("SELECT c FROM Customer c WHERE LOWER(c.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> searchByName(@Param("keyword") String keyword);

    @Query("SELECT SUM(c.balance) FROM Customer c")
    BigDecimal getTotalBalance();

    @Query(value = "SELECT COUNT(*) FROM customers", nativeQuery = true)
    long countCustomersNative();

    // සල්ලි යවන කෙනාගේ ගිණුමෙන් අඩුවීම (Balance එක ඇතිද බලලා තමයි update කරන්නේ)
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Customer c SET c.balance = c.balance - :amount WHERE c.accountNumber = :accountNumber AND c.balance >= :amount")
    int debit(@Param("accountNumber") String accountNumber, @Param("amount") BigDecimal amount);

    // සල්ලි ලැබෙන කෙනාගේ ගිණුමට එකතු වීම
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Customer c SET c.balance = c.balance + :amount WHERE c.accountNumber = :accountNumber")
    int credit(@Param("accountNumber") String accountNumber, @Param("amount") BigDecimal amount);
}