package ru.practicum.shareit.validationTest.gatewayUserController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.client.ShareItUserClient;
import ru.practicum.controller.GatewayUserController;
import ru.practicum.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GatewayUserController.class)
public class GatewayUserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShareItUserClient shareItUserClient;

    @Test
    void createNotValidUserTest() throws Exception {
        UserDto request = new UserDto();

        UserDto response = new UserDto();

        response.setName("Дима");

        when(shareItUserClient.createUser(any(UserDto.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateUserTest() throws Exception {
        UserDto request = new UserDto();
        request.setName("Дима");
        UserDto response = new UserDto();
        response.setName("Дима");

        when(shareItUserClient.updateUser(eq(1L), any(UserDto.class))).thenReturn(response);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()));
    }

    @Test
    void getUserTest() throws Exception {
        UserDto response = new UserDto();
        response.setName("Дима");

        when(shareItUserClient.getUserById(eq(1L))).thenReturn(response);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(response.getName()));
    }

    @Test
    void deleteUserByIdTest() throws Exception {
        doNothing().when(shareItUserClient).deleteUserById(eq(1L));
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
