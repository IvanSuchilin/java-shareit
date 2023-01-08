package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.exceptions.userExceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.userExceptions.InvalidEmailException;
import ru.practicum.shareit.exceptions.userExceptions.UserEmptyNameException;
import ru.practicum.shareit.exceptions.userExceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

class UserServiceIntTest {
    private static User user1;
    private static User user2;
    private static User wrongUser;

    @Autowired
    private  UserService userService;

    @BeforeAll
    public static void setup() {
        user1 = new User(null, "name1", "emailtestuser1@mail.ru");
        user2 = new User(null, "name2", "email2testuser@mail.ru");
        wrongUser =  new User(null, "", "");
    }
    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createTest() {
        UserDto storedUserDto = userService.create(user1);

        assertEquals(storedUserDto.getId(),1L);
        assertEquals(storedUserDto.getName(), "name1");
        assertEquals(storedUserDto.getEmail(), "emailtestuser1@mail.ru");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createUserEmailAlreadyExistTest() {
        userService.create(user1);
        User newUser = new User(null, "name1", "emailtestuser1@mail.ru");

        EmailAlreadyExistException thrown = Assertions.assertThrows(EmailAlreadyExistException.class,
                () -> userService.create(newUser));
        Assertions.assertEquals("Пользователь с такой почтой уже существует", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getUserByIdTest() {
        userService.create(user1);
        UserDto storedUser = userService.getUserById(1L);

        assertEquals(user1.getName(), storedUser.getName());
        assertEquals(user1.getEmail(), storedUser.getEmail());

    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getUserWrongIdTest() {

        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(1L));
        Assertions.assertEquals("Нет такого id", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateTest() {
        userService.create(user1);
        UserDto updatedUser = new UserDto(null, "updatedName", null);

        UserDto storedUser = userService.update(1L, updatedUser);

        assertEquals(storedUser.getName(), "updatedName");
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateWithWrongEmailTest() {
        userService.create(user1);
        UserDto updatedUser = new UserDto(null, "updatedName", "mail");

        InvalidEmailException thrown = Assertions.assertThrows(InvalidEmailException.class,
                () -> userService.update(1L, updatedUser));
        Assertions.assertEquals("Адрес электронной должен содержать символ @",thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateEmailAlreadyExistTest() {
        userService.create(user1);
        userService.create(user2);
        UserDto updatedUser = new UserDto(null,  null, "emailtestuser1@mail.ru");

        EmailAlreadyExistException thrown = Assertions.assertThrows(EmailAlreadyExistException.class,
                () -> userService.update(2L, updatedUser));
        Assertions.assertEquals("Пользователь с такой почтой уже существует", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void updateWrongUserTest() {
        UserDto updatedUser = new UserDto(null,  null, "e@mail.ru");

        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.update(1L, updatedUser));
        Assertions.assertEquals("Нет такого пользователя", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createWithEmptyEmailTest() {
        InvalidEmailException thrown = Assertions.assertThrows(InvalidEmailException.class,
                () -> userService.create(wrongUser));
        Assertions.assertEquals("Адрес электронной почты не может быть пустым и должен содержать символ @", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void createWithEmptyNameTest() {
        wrongUser.setEmail("e@mail");
        UserEmptyNameException thrown = Assertions.assertThrows(UserEmptyNameException.class,
                () -> userService.create(wrongUser));
        Assertions.assertEquals("Имя не может быть пустым", thrown.getMessage());
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void deleteTest() {
        userService.create(user1);
        userService.create(user2);

        userService.delete(2L);
        Collection<UserDto> allUsers = userService.getAllUsers();

        assertEquals(allUsers.size(), 1);
    }

    @Test
    @Sql(scripts = {"file:dbTest/scripts/schemaTest.sql"})
    void getAllUsersTest() {
        userService.create(user1);
        userService.create(user2);

        Collection<UserDto> allUsers = userService.getAllUsers();

        assertEquals(allUsers.size(), 2);
    }
}