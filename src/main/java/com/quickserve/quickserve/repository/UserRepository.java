package com.quickserve.quickserve.repository;

import com.quickserve.quickserve.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
