package com.krasnopolskyi.usersapitask.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class UserPutRequestDto {
    @Size(min = 2, max = 64)
    @NotBlank()
    private String firstname;

    @Size(min = 2, max = 64)
    @NotBlank()
    private String lastname;

    @NotNull
    @Past
    private LocalDate birthDate;

    @Size(min = 2, max = 255)
    private String address;

    @Size(min = 10, max = 32)
    private String phoneNumber;
}
