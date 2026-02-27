package com.quickserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.quickserve.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);
}