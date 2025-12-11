package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    public ItemDto createItem(Integer userId, ItemDto itemDto) {
        ItemDto returnableItem = ItemMapper.mapToItemDto(itemStorage.createItem(userId, ItemMapper.mapToItem(itemDto)));
        userService.addItemToUser(userId, itemDto);
        return returnableItem;
    }

    public ItemDto updateItem(Integer userId, int itemId, ItemDto itemDto) {
        userService.getUserByID(userId);
        return ItemMapper.mapToItemDto(itemStorage.updateItem(userId, itemId, ItemMapper.mapToItem(itemDto)));
    }

    public ItemDto getItem(int itemId) {
        return ItemMapper.mapToItemDto(itemStorage.getItem(itemId));
    }

    public List<ItemForUserDto> searchItem(Integer userId, String text) {
        return itemStorage.searchItem(userId, text).stream()
                .map(ItemMapper::mapToItemForUserDto).collect(Collectors.toList());
    }


}
