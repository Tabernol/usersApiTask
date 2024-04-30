package com.krasnopolskyi.usersapitask.service;

import com.krasnopolskyi.usersapitask.dto.UserPostRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserPatchRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserPutRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.utils.DateConvertor;
import com.krasnopolskyi.usersapitask.utils.UserMapper;
import com.krasnopolskyi.usersapitask.entity.User;
import com.krasnopolskyi.usersapitask.repository.UserRepository;
import com.krasnopolskyi.usersapitask.utils.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    /**
     * Retrieve a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return The user entity with the specified ID, if found.
     * @throws UserAppException If no user is found with the specified ID.
     */
    @Override
    public User getUserById(Long id) throws UserAppException {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    UserAppException exception = new UserAppException("Not found user with id " + id);
                    exception.setExceptionStatus(404);
                    return exception;
                });
    }

    /**
     * Create a new user with the provided details.
     *
     * @param userDto The DTO containing the details of the user to be created.
     * @return The created user entity.
     * @throws MinimumAgeException If the user's age does not meet the minimum age requirement.
     * @throws ValidationException If the email address provided is already exists.
     */
    @Override
    @Transactional
    public User createUser(UserPostRequestDto userDto) throws MinimumAgeException, ValidationException {
        userValidator.validateAge(userDto.getBirthDate());
        userValidator.validateEmail(userDto.getEmail());
        User user = UserMapper.mapToUser(userDto);
        return userRepository.save(user);
    }

    /**
     * Delete a user with the specified ID.
     *
     * @param id The ID of the user to delete.
     * @return {@code true} if the user was deleted successfully, {@code false} otherwise.
     */
    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        return userRepository.findById(id).
                map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                }).orElse(false);
    }

    /**
     * Update a user with the specified ID using the PUT method.
     *
     * @param id      The ID of the user to update.
     * @param userDto The DTO containing the updated user information.
     * @return The updated user.
     * @throws UserAppException if the user with the specified ID is not found or
     *                          age does not meet the minimum age requirement.
     */
    @Override
    @Transactional
    public User updatePut(Long id, UserPutRequestDto userDto) throws UserAppException {
        User user = getUserById(id);
        userValidator.validateAge(userDto.getBirthDate());

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setBirthDate(userDto.getBirthDate());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user = userRepository.save(user);
        return user;
    }

    /**
     * Partially updates a user with the specified ID using the PATCH method.
     *
     * <p>This method updates only the non-null fields of the existing user entity.</p>
     *
     * @param id      The ID of the user to update.
     * @param userDto The DTO containing the updated user information.
     * @return The updated user.
     * @throws UserAppException if the user with the specified ID is not found or
     *                          age does not meet the minimum age requirement.
     */
    @Override
    @Transactional
    public User updatePatch(Long id, UserPatchRequestDto userDto) throws UserAppException {
        User user = getUserById(id);
        // For this functionality 2.2. Update one/some user fields
        // I can use reflection, but it can make it difficult for reading and understanding
        // Update only non-null fields of the existing user entity
        if (userDto.getFirstname() != null) {
            user.setFirstname(userDto.getFirstname());
        }
        if (userDto.getLastname() != null) {
            user.setLastname(userDto.getLastname());
        }
        if (userDto.getBirthDate() != null) {
            userValidator.validateAge(userDto.getBirthDate());
            user.setBirthDate(userDto.getBirthDate());
        }
        if (userDto.getAddress() != null) {
            user.setAddress(userDto.getAddress());
        }
        if (userDto.getPhoneNumber() != null) {
            user.setPhoneNumber(userDto.getPhoneNumber());
        }
        user = userRepository.save(user);
        return user;
    }

    /**
     * Retrieves a list of users based on their birth dates within the specified date range.
     *
     * <p>If both 'from' and 'till' parameters are {@code null}, returns all users.</p>
     * <p>If both 'from' and 'till' parameters are provided, returns users with birth dates within the specified range.</p>
     * <p>If only 'from' parameter is provided, returns users with birth dates after the specified date.</p>
     * <p>If only 'to' parameter is provided, returns users with birth dates before the specified date.</p>
     *
     * @param from The start date of the period (inclusive), in the format 'yyyy-MM-dd'. Can be {@code null}.
     * @param till   The end date of the period (inclusive), in the format 'yyyy-MM-dd'. Can be {@code null}.
     * @return A list of users matching the specified birth date criteria.
     * @throws ValidationException if the provided date range is invalid.
     */
    @Override
    public List<User> getUsersByBirthDate(String from, String till) throws ValidationException {
        LocalDate startDate = DateConvertor.convertDate(from);
        LocalDate endDate = DateConvertor.convertDate(till);
        if (endDate == null && startDate == null) {
            return userRepository.findAll();
        } else if (startDate != null && endDate != null) {
            validatePeriod(startDate, endDate);
            return userRepository.findAllByBirthDateBetween(startDate, endDate);
        } else if (startDate != null) {
            return userRepository.findAllByBirthDateAfter(startDate);
        } else {
            return userRepository.findAllByBirthDateBefore(endDate);
        }
    }


    private void validatePeriod(LocalDate startDate, LocalDate endDate) throws ValidationException {
        if (startDate.isAfter(endDate)) {
            throw new ValidationException("Start date should be before end date");
        }
    }
}
