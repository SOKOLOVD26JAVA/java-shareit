package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UserIntegrationTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository repository;
    private UserDto userForTest;

    @BeforeEach
    void setUpUser() {
        userForTest = new UserDto();
        userForTest.setName("Дима");
        userForTest.setEmail("email@yandex.ru");
    }


    @Test
    void createUserTest() {
        UserDto dto = userService.createUser(userForTest);

        assertThat(dto.getId(), notNullValue());
        assertThat(dto.getName(), equalTo(userForTest.getName()));
        assertThat(dto.getEmail(), equalTo(userForTest.getEmail()));
    }

    @Test
    void updateUserTest() {
        UserDto savedUser = userService.createUser(userForTest);
        userForTest.setName("Не Дима");
        userForTest.setEmail("another@email.com");
        UserDto dto = userService.updateUser(savedUser.getId(), userForTest);

        assertThat(dto.getName(), equalTo(userForTest.getName()));
        assertThat(dto.getEmail(), equalTo(userForTest.getEmail()));
    }

    @Test
    void getUserByIdTest() {
        UserDto savedUser = userService.createUser(userForTest);

        UserDto dto = userService.getUserByID(savedUser.getId());

        assertThat(dto.getName(), notNullValue());
        assertThat(dto.getName(), equalTo(userForTest.getName()));
        assertThat(dto.getEmail(), equalTo(userForTest.getEmail()));
    }

    @Test
    void deleteUserByIdTest() {
        UserDto savedUser = userService.createUser(userForTest);

        userService.deleteUserByID(savedUser.getId());

        assertThatThrownBy(() -> userService.getUserByID(savedUser.getId())).isInstanceOf(NotFoundException.class);
    }

}
