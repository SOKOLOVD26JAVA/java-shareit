package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.model.Item;

public class ItemForUserMapper {

    public static ItemForUserDto mapToItemForUserDto(Item item) {
        ItemForUserDto itemForUserDto = new ItemForUserDto();

        itemForUserDto.setName(item.getName());
        itemForUserDto.setDescription(item.getDescription());

        return itemForUserDto;
    }
}
