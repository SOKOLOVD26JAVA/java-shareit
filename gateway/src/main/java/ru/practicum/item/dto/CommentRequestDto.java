package ru.practicum.item.dto;

import lombok.Data;
import ru.practicum.user.dto.UserDto;


import java.time.LocalDateTime;

@Data
public class CommentRequestDto {
    private Long id;
    private String text;
    private UserDto author;
    private ItemDto item;
    private LocalDateTime created;
}
