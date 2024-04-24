package com.krasnopolskyi.usersapitask.controller;

import com.krasnopolskyi.usersapitask.dto.UserRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.model.User;
import com.krasnopolskyi.usersapitask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody UserRequestDto userDto) throws MinimumAgeException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updatePatchUser(@PathVariable("id") Long id, @Validated @RequestBody UserRequestDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updatePutUser(@PathVariable("id") Long id, @Validated @RequestBody UserRequestDto userDto) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/id")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
//        return userService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        return null;
    }

    @GetMapping("/between_date")
    public ResponseEntity<List<User>> getUsersByPeriod() {
        return null;
    }
}
