package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.MissingHeaderException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.mapper.ItemForUserMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto getUserByID(Integer userId) {
        if (userId == null) {
            throw new MissingHeaderException("Отсутствует обязательный заголовок X-Sharer-User-Id.");
        }
        return UserMapper.mapUserDto(userStorage.getUserByID(userId));
    }

    public UserDto createUser(UserDto userDto) {
        return UserMapper.mapUserDto(userStorage.createUser(UserMapper.mapToUser(userDto)));
    }

    public UserDto updateUser(int userId, UserDto userDto) {
        return UserMapper.mapUserDto(userStorage.updateUser(userId, UserMapper.mapToUser(userDto)));
    }

    public void deleteUserByID(int userId) {
        userStorage.deleteUserByID(userId);
    }

    public void addItemToUser(int userId, ItemDto itemDto) {
        userStorage.addItemToUser(userId, ItemMapper.mapToItem(itemDto));
    }

    public List<ItemForUserDto> getUserItemList(int userId) {
        return userStorage.getUserItemList(userId).stream()
                .map(ItemForUserMapper::mapToItemForUserDto).collect(Collectors.toList());
    }
}
