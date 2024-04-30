package com.krasnopolskyi.usersapitask.controller;

import com.krasnopolskyi.usersapitask.dto.UserPostRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserPatchRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserPutRequestDto;
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
    /**
     * Retrieve a user by their unique identifier.
     *
     * @param id The unique identifier of the user to retrieve.
     * @return ResponseEntity containing the user information if found, or an HTTP 404 Not Found status if the user does not exist.
     * @throws UserAppException If an error occurs while retrieving the user.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) throws UserAppException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }
    /**
     * Create a new user with the provided user data.
     *
     * @param userDto The data of the user to be created.
     * @return ResponseEntity containing the created user information with an HTTP 201 Created status.
     * @throws MinimumAgeException  If the user's age is below the minimum allowed age.
     * @throws ValidationException If the user data fails validation checks.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@Validated @RequestBody UserPostRequestDto userDto)
            throws MinimumAgeException, ValidationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }
    /**
     * Update the specified user with the provided partial user data.
     *
     * @param id      The ID of the user to be updated.
     * @param userDto The partial data of the user to be updated.
     * @return ResponseEntity containing the updated user information with an HTTP 200 OK status.
     * @throws UserAppException If an error occurs while updating the user.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> updatePatchUser(
            @PathVariable("id") Long id,
            @Validated @RequestBody UserPatchRequestDto userDto) throws UserAppException {
        User user = userService.updatePatch(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    /**
     * Fully update the specified user using the provided user data
     *
     * @param id      The ID of the user to be updated.
     * @param userDto The data of the user to be updated.
     * @return ResponseEntity containing the updated user information with an HTTP 200 OK status.
     * @throws UserAppException If an error occurs while updating the user.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updatePutUser(
            @PathVariable("id") Long id,
            @Validated @RequestBody UserPutRequestDto userDto) throws UserAppException {
        User user = userService.updatePut(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    /**
     * Delete the user with the specified ID.
     *
     * @param id The ID of the user to be deleted.
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT) if the user is successfully deleted,
     *         or HTTP status 404 (NOT_FOUND) if the user with the given ID does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    /**
     * Retrieve a list of users based on the specified date range.
     *
     * @param startDate The start date of the range (optional). If provided, only users with birth dates
     *                  on or after this date will be included.
     * @param endDate   The end date of the range (optional). If provided, only users with birth dates
     *                  on or before this date will be included.
     * @return ResponseEntity with HTTP status 200 (OK) and a list of users within the specified date range,
     *         or HTTP status 400 (BAD_REQUEST) if the provided dates are invalid.
     * @throws ValidationException If the provided start date is after the end date.
     */
    @GetMapping("/range")
    public ResponseEntity<List<User>> getUsersByPeriod(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) throws ValidationException {
        List<User> users = userService.getUsersByBirthDate(startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
}
