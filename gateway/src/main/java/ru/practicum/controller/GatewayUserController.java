package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.ShareItUserClient;
import ru.practicum.user.dto.UserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class GatewayUserController {

    private final ShareItUserClient shareItClient;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        return shareItClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        return shareItClient.updateUser(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return shareItClient.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserByID(@PathVariable Long userId) {
        shareItClient.deleteUserById(userId);
    }

}
