package com.krasnopolskyi.usersapitask.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class UserPatchRequestDto {
    @Size(min = 2, max = 64)
    private String firstname;

    @Size(min = 2, max = 64)
    private String lastname;

    @Past
    private LocalDate birthDate;

    @Size(min = 2, max = 255)
    private String address;

    @Size(min = 10, max = 32)
    private String phoneNumber;
}
