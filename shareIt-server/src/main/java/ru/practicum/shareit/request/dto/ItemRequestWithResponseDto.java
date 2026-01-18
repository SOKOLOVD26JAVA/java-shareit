package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestWithResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Long requesterId;
    private List<ItemForRequestDto> items;
}
