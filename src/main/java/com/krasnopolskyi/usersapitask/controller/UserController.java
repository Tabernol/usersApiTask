package com.krasnopolskyi.usersapitask.controller;

import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserUpdateRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.entity.User;
import com.krasnopolskyi.usersapitask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) throws UserAppException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody UserCreateRequestDto userDto)
            throws MinimumAgeException, ValidationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updatePatchUser(
            @PathVariable("id") Long id,
            @Validated @RequestBody UserUpdateRequestDto userDto) throws UserAppException {
        User user = userService.updateUserNotNullFields(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updatePutUser(
            @PathVariable("id") Long id,
            @Validated @RequestBody UserUpdateRequestDto userDto) throws UserAppException {
        User user = userService.updateUser(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/range")
    public ResponseEntity<List<User>> getUsersByPeriod(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) throws ValidationException {
        List<User> users = userService.getUsersByBirthDate(startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
