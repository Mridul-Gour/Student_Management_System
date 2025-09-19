package com.cts.sms.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.sms.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
