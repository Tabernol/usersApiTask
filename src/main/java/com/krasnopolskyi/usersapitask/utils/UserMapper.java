package com.krasnopolskyi.usersapitask.utils;

import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.entity.User;

public class UserMapper {

    public static User mapToUser(UserCreateRequestDto userDto) {
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
