package com.krasnopolskyi.usersapitask.service;

import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserUpdateRequestDto;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.mapper.UserMapper;
import com.krasnopolskyi.usersapitask.model.User;
import com.krasnopolskyi.usersapitask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Value("${age.minimum}")
    private Integer minimumAge;

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long id) throws UserAppException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserAppException("Not found user with id " + id));
    }

    @Override
    @Transactional
    public User createUser(UserCreateRequestDto userDto) throws MinimumAgeException, ValidationException {
        validateAge(userDto.getBirthDate());
        validateEmail(userDto.getEmail());
        User user = UserMapper.mapToUser(userDto);
        return userRepository.save(user);
    }


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

    @Override
    @Transactional
    public User updateUser(Long id, UserUpdateRequestDto userDto) throws UserAppException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserAppException("Not found user with id " + id));

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setBirthDate(userDto.getBirthDate());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user = userRepository.save(user);
        return user;
    }

    @Override
    public List<User> getUsersByBirthDate(LocalDate from, LocalDate to) {
        return null;
    }


    private void validateAge(LocalDate dateOfBirth) throws MinimumAgeException {
        Period age = Period.between(dateOfBirth, LocalDate.now());
        // Check if the person is  up to 18 years old
        if (age.getYears() < minimumAge) {
            throw new MinimumAgeException("Age up to 18 years. Sorry, but I couldn't register you");
        }
    }

    private void validateEmail(String email) throws ValidationException {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("The email address " + email + " already exists.");
        }
    }
}
