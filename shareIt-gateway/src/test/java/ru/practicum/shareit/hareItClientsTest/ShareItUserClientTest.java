package ru.practicum.shareit.hareItClientsTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.Paths;
import ru.practicum.client.ShareItUserClient;
import ru.practicum.user.dto.UserDto;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ShareItUserClientTest {
    @Autowired
    private ShareItUserClient client;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${shareit.server.url}")
    private String serverUrl;

    @Test
    void createUserTest() {
        UserDto user = new UserDto();
        user.setName("тест");

        when(restTemplate.exchange(eq(serverUrl + Paths.USER)
                , eq(HttpMethod.POST), any(HttpEntity.class), eq(UserDto.class)))
                .thenReturn(new ResponseEntity<>(user, HttpStatus.OK));
        client.createUser(user);
    }

    @Test
    void updateUserTest() {
        UserDto user = new UserDto();
        user.setName("тест");

        when(restTemplate.exchange(eq(serverUrl + Paths.USER + "/1")
                , eq(HttpMethod.PATCH), any(HttpEntity.class), eq(UserDto.class), any(Long.class)))
                .thenReturn(new ResponseEntity<>(user, HttpStatus.OK));
        client.updateUser(1L, user);
    }

    @Test
    void getUserByIdTest() {
        when(restTemplate.getForObject(eq(serverUrl + Paths.USER + "/1"), eq(UserDto.class), any(Long.class)))
                .thenReturn(new UserDto());

        client.getUserById(1L);
    }

    @Test
    void deleteUserByIdTest() {
        doNothing().when(restTemplate).delete(eq(serverUrl + Paths.USER + "/1"), any(Long.class));

        client.deleteUserById(1L);
    }
}
