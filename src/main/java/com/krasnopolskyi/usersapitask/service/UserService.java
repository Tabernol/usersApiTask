package com.krasnopolskyi.usersapitask.service;

import com.krasnopolskyi.usersapitask.dto.UserPostRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserPatchRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserPutRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.entity.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id) throws UserAppException;

    User createUser(UserPostRequestDto userDto) throws MinimumAgeException, ValidationException;

    boolean deleteUser(Long id);

    User updatePut(Long id, UserPutRequestDto userDto) throws UserAppException;

    User updatePatch(Long id, UserPatchRequestDto userDto) throws UserAppException;

    List<User> getUsersByBirthDate(String from, String to) throws ValidationException; // two date between

}
