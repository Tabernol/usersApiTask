package com.krasnopolskyi.usersapitask.service;

import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserUpdateRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.entity.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id) throws UserAppException;

    User createUser(UserCreateRequestDto userDto) throws MinimumAgeException, ValidationException;

    boolean deleteUser(Long id);

    User updateUser(Long id, UserUpdateRequestDto userDto) throws UserAppException;

    User updateUserNotNullFields(Long id, UserUpdateRequestDto userDto) throws UserAppException;

    List<User> getUsersByBirthDate(String from, String to) throws ValidationException; // two date between

}
