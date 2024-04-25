package com.krasnopolskyi.usersapitask.repository;

import com.krasnopolskyi.usersapitask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
