package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;


public class UserMapper {


    public static UserDto mapUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public static User mapToUser(UserDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }
}
