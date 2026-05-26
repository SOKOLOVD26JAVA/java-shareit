package ru.practicum.shareit.item.dto;


import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserDto owner;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<CommentResponseDto> comments;
    private Long requestId;
}
