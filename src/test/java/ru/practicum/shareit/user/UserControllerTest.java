package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

class UserControllerTest {
    private UserDto userDto;
    private UserDto user2Dto;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "Test", "Test@mail.ru");
        // user2Dto = new UserDto(2L, "Test2", "Test2@mail.ru");
    }

    @SneakyThrows
    @Test
    void getUserByIdTest() {
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk());
        verify(userService).getUserById(1L);
    }

    @Test
    void create() throws Exception {
        when(userService.create(any()))
                .thenReturn(userDto);

        //String result =
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test"));

    }

    @Test
    void findAll() throws Exception {
        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        //users.add(user2Dto);
        when(userService.getAllUsers())
                .thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(userDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", is(userDto.getEmail())));
    }
}