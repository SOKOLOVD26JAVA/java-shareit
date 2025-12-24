package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {

        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }


    public static Item mapToItem(ItemDto itemDto) {
        Item item = new Item();
        Optional<Integer> itemId = Optional.ofNullable(itemDto.getId());
        if (itemId.isPresent()) {
            item.setId(itemDto.getId());
        }
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    public static ItemForUserDto mapToItemForUserDto(Item item) {
        ItemForUserDto itemForUserDto = new ItemForUserDto();

        itemForUserDto.setName(item.getName());
        itemForUserDto.setDescription(item.getDescription());

        return itemForUserDto;
    }
}
