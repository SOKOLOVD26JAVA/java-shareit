package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUserByID(Long userId) {
        return UserMapper.mapUserDto(userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        return UserMapper.mapUserDto(userRepository.save(UserMapper.mapToUser(userDto)));
    }

    @Transactional
    public UserDto updateUser(Long userId, UserDto userDto) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));

        if (userDto.getName() != null) {
            findUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            findUser.setEmail(userDto.getEmail());
        }

        return UserMapper.mapUserDto(userRepository.save(findUser));
    }

    @Transactional
    public void deleteUserByID(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userRepository.deleteById(userId);
    }

    public List<ItemForUserDto> getUserItemList(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));

        return findUser.getItemList().stream().map(ItemMapper::mapToItemForUserDto).collect(Collectors.toList());

    }
}
