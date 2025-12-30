package ru.practicum.shareit.item.dto;


import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class CommentRequestDto {
    private Long id;
    private String text;
    private User author;
    private Item item;
    private LocalDateTime created;
}
