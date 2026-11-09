package com.example.bankapp.config;

import com.example.bankapp.model.Customer;
import com.example.bankapp.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(CustomerRepository repository) {
        return args -> {
            // ඩේටාබේස් එකේ කලින් කස්ටමර්ස්ලා නැත්නම් විතරක් මේ 3 දෙනාව ඇතුළත් කරනවා
            if (repository.count() == 0) {
                repository.save(new Customer(null, "100001", "Kamal", "kamal@gmail.com", new BigDecimal("50000.00")));
                repository.save(new Customer(null, "100002", "Nimali", "nimali@gmail.com", new BigDecimal("25000.00")));
                repository.save(new Customer(null, "100003", "Saman", "saman@gmail.com", new BigDecimal("10000.00")));
            }
        };
    }
}