package com.krasnopolskyi.usersapitask.utils;

import com.krasnopolskyi.usersapitask.dto.UserPostRequestDto;
import com.krasnopolskyi.usersapitask.entity.User;

public class UserMapper {

    private UserMapper() {
        // Private constructor to prevent instantiation
    }

    public static User mapToUser(UserPostRequestDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .birthDate(userDto.getBirthDate())
                .address(userDto.getAddress())
                .phoneNumber(userDto.getPhoneNumber())
                .build();
    }
}
