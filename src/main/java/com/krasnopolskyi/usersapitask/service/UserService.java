package com.krasnopolskyi.usersapitask.service;

import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserUpdateRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User getUserById(Long id) throws UserAppException;

    User createUser(UserCreateRequestDto userDto) throws MinimumAgeException, ValidationException;

    boolean deleteUser(Long id);

    User updateUser(Long id, UserUpdateRequestDto userDto) throws UserAppException;

    List<User> getUsersByBirthDate(LocalDate from, LocalDate to); // two date between

}
