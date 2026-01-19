package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithOutResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(itemRequestDto.getDescription());

        return itemRequest;
    }

    public static ItemRequestWithOutResponseDto mapToItemRequestWithOutResponseDto(ItemRequest itemRequest) {
        ItemRequestWithOutResponseDto itemRequestDto = new ItemRequestWithOutResponseDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setRequesterId(itemRequest.getRequester().getId());

        return itemRequestDto;
    }

    public static ItemRequestWithResponseDto mapToItemRequestWithResponseDto(ItemRequest itemRequest) {
        ItemRequestWithResponseDto itemRequestDto = new ItemRequestWithResponseDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setRequesterId(itemRequest.getRequester().getId());
        if (itemRequest.getItems() == null || itemRequest.getItems().isEmpty()) {
            itemRequest.setItems(new ArrayList<>());
        } else {
            itemRequestDto.setItems(itemRequest.getItems().stream().map(ItemMapper::mapToItemForRequestDto).collect(Collectors.toList()));
        }
        return itemRequestDto;
    }
}
