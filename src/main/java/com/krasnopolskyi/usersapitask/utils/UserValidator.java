package com.krasnopolskyi.usersapitask.utils;

import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
@Component
public class UserValidator {
    private final UserRepository userRepository;

    @Value("${age.minimum}")
    private static Integer minimumAge;

    public void validateAge(LocalDate dateOfBirth) throws MinimumAgeException {
        if (dateOfBirth != null) {
            Period age = Period.between(dateOfBirth, LocalDate.now());
            // Check if the person is  up to 18 years old
            if (age.getYears() < minimumAge) {
                throw new MinimumAgeException("Age up to 18 years. Sorry, but we couldn't register you");
            }
        }
    }

    public void validateEmail(String email) throws ValidationException {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("The email address " + email + " already exists.");
        }

        // OWASP Validation Regular Expression https://owasp.org/www-community/OWASP_Validation_Regex_Repository
        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(regexPattern)) {
            throw new ValidationException("Invalid email address format: " + email);
        }
    }
}
