package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Optional;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {

        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(UserMapper.mapUserDto(item.getOwner()));
        if (item.getComments() != null) {
            itemDto.setComments(item.getComments().stream().map(CommentMapper::mapToCommentResponseDto).collect(Collectors.toList()));
        }
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }

        return itemDto;
    }


    public static Item mapToItem(ItemDto itemDto) {
        Item item = new Item();
        Optional<Long> itemId = Optional.ofNullable(itemDto.getId());
        if (itemId.isPresent()) {
            item.setId(itemDto.getId());
        }
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(null);

        return item;
    }

    public static ItemForUserDto mapToItemForUserDto(Item item) {
        ItemForUserDto itemForUserDto = new ItemForUserDto();

        itemForUserDto.setName(item.getName());
        itemForUserDto.setDescription(item.getDescription());

        return itemForUserDto;
    }

    public static ItemForRequestDto mapToItemForRequestDto(Item item) {
        ItemForRequestDto dto = new ItemForRequestDto();

        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getOwner().getId());

        return dto;
    }
}
