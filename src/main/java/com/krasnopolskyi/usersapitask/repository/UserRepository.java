package com.krasnopolskyi.usersapitask.repository;

import com.krasnopolskyi.usersapitask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    List<User> findAllByBirthDateAfter(LocalDate date);

    List<User> findAllByBirthDateBefore(LocalDate date);

    List<User> findAllByBirthDateBetween(LocalDate startDate, LocalDate endDate);
}
