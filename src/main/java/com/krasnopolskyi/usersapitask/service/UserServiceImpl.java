package com.krasnopolskyi.usersapitask.service;

import com.krasnopolskyi.usersapitask.dto.UserRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.model.User;
import com.krasnopolskyi.usersapitask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${age.minimum}")
    private Integer minimumAge;

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public User createUser(UserRequestDto userDto) throws MinimumAgeException {
        log.info("Minimum age exception is " + minimumAge.toString());
        validateAge(userDto.getBirthDate());

        return null;
    }


    @Override
    public boolean deleteUser(Long id) {
        return false;
    }

    @Override
    public User updateUser(UserRequestDto user) {
        return null;
    }

    @Override
    public List<User> getUsersByBirthDate(LocalDate from, LocalDate to) {
        return null;
    }


    private void validateAge(LocalDate dateOfBirth) throws MinimumAgeException {
        Period age = Period.between(dateOfBirth, LocalDate.now());
        // Check if the person is 18 years old or older
        if (age.getYears() < 18) {
            throw new MinimumAgeException("Age up to 18 years. Sorry, but I couldn't register you");
        }
    }
}
