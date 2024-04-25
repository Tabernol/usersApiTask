package com.krasnopolskyi.usersapitask.controller;

import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserUpdateRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.model.User;
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

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody UserCreateRequestDto userDto)
            throws MinimumAgeException, ValidationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<User> updatePatchUser(
//            @PathVariable("id") Long id,
//            @Validated @RequestBody UserUpdateRequestDto userDto) {
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updatePutUser(
            @PathVariable("id") Long id,
            @Validated @RequestBody UserUpdateRequestDto userDto) throws UserAppException {
        User user = userService.updateUser(id, userDto);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/id")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/range/from/{startDate}/to/{endDate}")
    public ResponseEntity<List<User>> getUsersByPeriod(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        log.info("FROM " + startDate);
        log.info("to " + endDate);
        return null;
    }
}
