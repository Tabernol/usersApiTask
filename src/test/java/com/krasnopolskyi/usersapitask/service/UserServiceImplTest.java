package com.krasnopolskyi.usersapitask.service;

import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.dto.UserUpdateRequestDto;
import com.krasnopolskyi.usersapitask.entity.User;
import com.krasnopolskyi.usersapitask.exception.MinimumAgeException;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    private void setUp() {
        userService = new UserServiceImpl(userRepository);
        ReflectionTestUtils.setField(userService, "minimumAge", 18); // mock value from application.yaml
        user = User.builder()
                .id(1L)
                .email("johngold@gold.ua")
                .firstname("John")
                .lastname("Gold")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("Earth")
                .phoneNumber("1234567890")
                .build();
    }

    @Test
    void getUserById_ReturnUser_whenExists() throws UserAppException {
        // Arrange
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        // Act
        User testUser = userService.getUserById(1L);
        // Assert
        assertEquals(user, testUser);
    }

    @Test
    void getUserById_ThrowException_whenNotExists() throws UserAppException {
        // Arrange
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        //  Act&Assert
        assertThrows(UserAppException.class, () -> userService.getUserById(1L));
    }

    @Test
    void deleteUser_ReturnFalse_whenExists() {
        // Arrange
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.deleteUser(1L);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
        verify(userRepository, times(1)).flush();
    }

    @Test
    void deleteUser_ReturnFalse_WhenNotExists() {
        // Arrange
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Act
        boolean result = userService.deleteUser(1L);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).delete(user);
        verify(userRepository, never()).flush();
    }

    @Test
    void getUsersByBirthDate_ReturnsAllUsers_WhenBothDatesNull() throws ValidationException {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act
        List<User> result = userService.getUsersByBirthDate(null, null);

        // Assert
        assertEquals(List.of(user), result);
    }

    @Test
    void getUsersByBirthDate_ReturnsUsersInPeriod_WhenBothDatesProvided() throws ValidationException {
        // Arrange
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.of(2001, 1, 1);
        when(userRepository.findAllByBirthDateBetween(startDate, endDate)).thenReturn(List.of(user));

        // Act
        List<User> result = userService.getUsersByBirthDate(startDate.toString(), endDate.toString());

        // Assert
        assertEquals(List.of(user), result);
    }

    @Test
    void getUsersByBirthDate_ThrowException_WhenBothDatesProvided() throws ValidationException {
        // Arrange
        LocalDate startDate = LocalDate.of(2003, 1, 1);
        LocalDate endDate = LocalDate.of(2001, 1, 1);

        // Act&Assert
        assertThrows(ValidationException.class, () ->
                userService.getUsersByBirthDate(startDate.toString(), endDate.toString()));
    }

    @Test
    void getUsersByBirthDate_ReturnsUsersAfterStartDate_WhenStartDateProvided() throws ValidationException {
        // Arrange
        LocalDate startDate = LocalDate.of(2000, 1, 1);
        when(userRepository.findAllByBirthDateAfter(startDate)).thenReturn(List.of(user));

        // Act
        List<User> result = userService.getUsersByBirthDate(startDate.toString(), null);

        // Assert
        assertEquals(List.of(user), result);
    }

    @Test
    void getUsersByBirthDate_ReturnsUsersBeforeEndDate_WhenEndDateProvided() throws ValidationException {
        // Arrange
        LocalDate endDate = LocalDate.of(2000, 1, 1);
        when(userRepository.findAllByBirthDateBefore(endDate)).thenReturn(List.of(user));

        // Act
        List<User> result = userService.getUsersByBirthDate(null, endDate.toString());

        // Assert
        assertEquals(List.of(user), result);
    }

    @Test
    void createUser_ReturnsUser_WhenValidDto() throws MinimumAgeException, ValidationException {
        // Arrange
        UserCreateRequestDto userDto = UserCreateRequestDto.builder()
                .email("johngold@gold.ua")
                .firstname("John")
                .lastname("Gold")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("Earth")
                .phoneNumber("1234567890")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.createUser(userDto);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getFirstname(), result.getFirstname());
        assertEquals(userDto.getLastname(), result.getLastname());
        assertEquals(userDto.getBirthDate(), result.getBirthDate());
        assertEquals(userDto.getAddress(), result.getAddress());
        assertEquals(userDto.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void createUser_ThrowsMinimumAgeException_WhenUnderage() {
        // Arrange
        UserCreateRequestDto userDto = UserCreateRequestDto.builder()
                .email("johngold@gold.ua")
                .firstname("John")
                .lastname("Gold")
                .birthDate(LocalDate.now().minusYears(17))
                .address("Earth")
                .phoneNumber("1234567890")
                .build();
        // Act&Assert
        assertThrows(MinimumAgeException.class, () -> userService.createUser(userDto));
    }

    @Test
    void createUser_ThrowsValidateException_WhenEmailExists() {
        // Arrange
        UserCreateRequestDto userDto = UserCreateRequestDto.builder()
                .email("johngold@gold.ua")
                .firstname("John")
                .lastname("Gold")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("Earth")
                .phoneNumber("1234567890")
                .build();
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act&Assert
        assertThrows(ValidationException.class, () -> userService.createUser(userDto));
    }


    @Test
//PUT
    void testUpdateUser_ReturnUser_ValidDto() throws UserAppException {
        // Arrange
        Long userId = 1L;
        UserUpdateRequestDto userDto = UserUpdateRequestDto.builder()
                .firstname("John2")
                .lastname("Doe")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address("123 Main St")
                .phoneNumber("0987654321")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.updateUser(userId, userDto);

        // Assert
        assertEquals(userId, updatedUser.getId());
        assertEquals(userDto.getFirstname(), updatedUser.getFirstname());
        assertEquals(userDto.getLastname(), updatedUser.getLastname());
        assertEquals(userDto.getBirthDate(), updatedUser.getBirthDate());
        assertEquals(userDto.getAddress(), updatedUser.getAddress());
        assertEquals(userDto.getPhoneNumber(), updatedUser.getPhoneNumber());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
//PUT
    void testUpdateUser_ReturnUser_InValidDto() throws UserAppException {
        // Arrange
        Long userId = 1L;
        UserUpdateRequestDto userDto = UserUpdateRequestDto.builder()
                .firstname("John2")
                .lastname("Doe")
                .birthDate(LocalDate.of(2010, 5, 15))
                .address("123 Main St")
                .phoneNumber("0987654321")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act&Assert
        assertThrows(MinimumAgeException.class, () -> userService.updateUser(userId, userDto));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test//PATCH
    void testUpdateUserNotNullFields_ReturnUser_ValidDto() throws UserAppException {
        // Arrange
        Long userId = 1L;
        UserUpdateRequestDto userDto = UserUpdateRequestDto.builder()
                .firstname("Tom")
                .birthDate(LocalDate.of(1990, 5, 15))
                .phoneNumber("0987654321")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.updateUserNotNullFields(userId, userDto);

        // Assert
        assertEquals(userId, updatedUser.getId());
        assertEquals(userDto.getFirstname(), updatedUser.getFirstname());
        assertEquals(user.getLastname(), updatedUser.getLastname()); // Should not be updated
        assertEquals(userDto.getBirthDate(), updatedUser.getBirthDate());
        assertEquals(user.getAddress(), updatedUser.getAddress()); // Should not be updated
        assertEquals(userDto.getPhoneNumber(), updatedUser.getPhoneNumber());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test//PATCH
    void testUpdateUserNotNullFields_ReturnUser_ValidDtoAnotherFields() throws UserAppException {
        // Arrange
        Long userId = 1L;
        UserUpdateRequestDto userDto = UserUpdateRequestDto.builder()
                .lastname("Smith")
                .address("Mars")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.updateUserNotNullFields(userId, userDto);

        // Assert
        assertEquals(userId, updatedUser.getId());
        assertEquals(user.getFirstname(), updatedUser.getFirstname());// Should not be updated
        assertEquals(updatedUser.getLastname(), updatedUser.getLastname());
        assertEquals(user.getBirthDate(), updatedUser.getBirthDate());// Should not be updated
        assertEquals(updatedUser.getAddress(), updatedUser.getAddress());
        assertEquals(user.getPhoneNumber(), updatedUser.getPhoneNumber());// Should not be updated

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
//PATCH
    void testUpdateUserNotNullFields_ThrowException_InValidDto() throws UserAppException {
        // Arrange
        Long userId = 1L;
        UserUpdateRequestDto userDto = UserUpdateRequestDto.builder()
                .firstname("Tom")
                .birthDate(LocalDate.of(2020, 5, 15))
                .phoneNumber("0987654321")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));


        // Act&Assert
        assertThrows(MinimumAgeException.class, () -> userService.updateUserNotNullFields(userId, userDto));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

}
