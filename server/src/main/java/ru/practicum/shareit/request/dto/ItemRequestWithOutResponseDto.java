package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestWithOutResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Long requesterId;

}
