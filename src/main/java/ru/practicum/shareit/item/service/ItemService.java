package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.MissingHeaderException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.mapper.ItemForUserMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;

    public ItemDto createItem(Integer userId, ItemDto itemDto) {
        headerCheck(userId);
        return ItemMapper.mapToItemDto(itemStorage.createItem(userId, ItemMapper.mapToItem(itemDto)));
    }

    public ItemDto updateItem(Integer userId, int itemId, ItemDto itemDto) {
        headerCheck(userId);
        return ItemMapper.mapToItemDto(itemStorage.updateItem(userId, itemId, ItemMapper.mapToItem(itemDto)));
    }

    public ItemDto getItem(int itemId) {
        return ItemMapper.mapToItemDto(itemStorage.getItem(itemId));
    }

    public List<ItemForUserDto> searchItem(Integer userId, String text) {
        if (text.equals("missing")) {
            return new ArrayList<>();
        }
        return itemStorage.searchItem(userId, text).stream()
                .map(ItemForUserMapper::mapToItemForUserDto).collect(Collectors.toList());
    }

    private void headerCheck(Integer userId) {
        if (userId == null) {
            throw new MissingHeaderException("Отсутствует обязательный заголовок X-Sharer-User-Id.");
        }
    }


}
