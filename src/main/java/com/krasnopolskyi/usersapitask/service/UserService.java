package com.krasnopolskyi.usersapitask.service;

import com.krasnopolskyi.usersapitask.dto.UserRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User getUserById(Long id);

    User createUser(UserRequestDto userDto) throws MinimumAgeException;

    boolean deleteUser(Long id);

    User updateUser(UserRequestDto userDto);

    List<User> getUsersByBirthDate(LocalDate from, LocalDate to); // two date between

}
