package com.krasnopolskyi.usersapitask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krasnopolskyi.usersapitask.dto.UserCreateRequestDto;
import com.krasnopolskyi.usersapitask.entity.User;
import com.krasnopolskyi.usersapitask.exception.UserAppException;
import com.krasnopolskyi.usersapitask.exception.ValidationException;
import com.krasnopolskyi.usersapitask.service.UserService;
import com.krasnopolskyi.usersapitask.service.UserServiceImpl;
import com.krasnopolskyi.usersapitask.utils.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    private User user;
    private User user2;

    @BeforeEach
    private void setUp() {
        mapper.registerModule(new JavaTimeModule());
        userController = new UserController(userService);
        user = User.builder()
                .id(1L)
                .email("johngold@gold.ua")
                .firstname("John")
                .lastname("Gold")
                .birthDate(LocalDate.of(2000, 1, 1))
                .address("Earth")
                .phoneNumber("1234567890")
                .build();
        user2 = User.builder()
                .id(2L)
                .email("silver@gold.ua")
                .firstname("Tom")
                .lastname("Silver")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("Mars")
                .phoneNumber("0987654321")
                .build();
    }

    @Test
    void getUser_ReturnsUser_WhenIdExists() throws Exception {
        // Arrange
        Long userId = 1L;
        given(userService.getUserById(userId)).willReturn(user);

        // Act&assert
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.firstname", is(user.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(user.getLastname())))
                .andExpect(jsonPath("$.birthDate", is(user.getBirthDate().toString())))
                .andExpect(jsonPath("$.address", is(user.getAddress())))
                .andExpect(jsonPath("$.phoneNumber", is(user.getPhoneNumber())));

        verify(userService, times(1)).getUserById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void getUser_ReturnsNotFound_WhenIdDoesNotExist() throws Exception {
        // Arrange
        Long userId = 1L;
        UserAppException exception = new UserAppException("Not found user with id " + userId);
        exception.setExceptionStatus(404);
        given(userService.getUserById(userId)).willThrow(exception);

        //Act&assert
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testDeleteUser_Success_WhenUserExists() throws Exception {
        // Arrange
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn(true);

        // Act&assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isNoContent())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Content-Type"));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void testDeleteUser_NotFound_WhenUserDoesNotExist() throws Exception {
        // Arrange
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn(false);

        // Act&assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andExpect(MockMvcResultMatchers.header().doesNotExist("Content-Type"));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void getUsersByPeriod_ReturnsListOfUsers_WhenValidDatesProvided() throws Exception {
        // Arrange
        List<User> users = Arrays.asList(user, user2);
        when(userService.getUsersByBirthDate(anyString(), anyString())).thenReturn(users);

        // Act&Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/range")
                        .param("startDate", "1990-01-01")
                        .param("endDate", "2022-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstname").value("John"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstname").value("Tom"));

    }

    @Test
    void getUsersByPeriod_ReturnsBadRequest_WhenInvalidDatesProvided() throws Exception {
        //Arrange
        when((userService.getUsersByBirthDate(anyString(), anyString())))
                .thenThrow(new ValidationException("Date is invalid"));
        // Act&Assert
        mockMvc.perform(get("/users/range")
                        .param("startDate", "invalid-date")
                        .param("endDate", "invalid-date")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Expecting status code 400 Bad Request
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/user_data.csv", numLinesToSkip = 1)
    void testCreateUser_ReturnUser_ValidDto(String email,
                                            String firstname,
                                            String lastname,
                                            String birthDate,
                                            String address,
                                            String phoneNumber) throws Exception {

        UserCreateRequestDto userDto = UserCreateRequestDto.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .birthDate(LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE))
                .address(address)
                .phoneNumber(phoneNumber)
                .build();

        User currentUser = UserMapper.mapToUser(userDto);
        when(userService.createUser(any(UserCreateRequestDto.class))).thenReturn(currentUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(currentUser.getEmail()))
                .andExpect(jsonPath("$.lastname").value(currentUser.getLastname()));
        verify(userService, times(1)).createUser(any(UserCreateRequestDto.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/user_data_invalid.csv", numLinesToSkip = 1)
    void testCreateUser_ThrowException_InValidDto(String email,
                                                  String firstname,
                                                  String lastname,
                                                  String birthDate,
                                                  String address,
                                                  String phoneNumber) throws Exception {

        UserCreateRequestDto userDto = UserCreateRequestDto.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .birthDate(LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE))
                .address(address)
                .phoneNumber(phoneNumber)
                .build();

        User currentUser = UserMapper.mapToUser(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createUser(any(UserCreateRequestDto.class));
    }
}
