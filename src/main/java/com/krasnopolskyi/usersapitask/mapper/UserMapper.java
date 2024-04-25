package com.krasnopolskyi.usersapitask.mapper;

import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserUpdateRequestDto;
import com.krasnopolskyi.usersapitask.model.User;

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
