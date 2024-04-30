package com.krasnopolskyi.usersapitask.utils;

import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.repository.UserRepository;
import com.krasnopolskyi.usersapitask.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserValidator userValidator;

    @BeforeEach
    private void setUp() {
        userValidator = new UserValidator(userRepository);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalid-email.csv", numLinesToSkip = 1)
    void validateEmail_ThrowException_InvalidEmail(String email) throws ValidationException {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        // Act&Assert

        assertThrows(ValidationException.class, () -> userValidator.validateEmail(email));
    }
}
