package ru.practicum.item.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemDto {
    private Long id;
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание не может быть пустым.")
    @Size(max = 200, message = "Описание не более 200 символов.")
    private String description;
    @NotNull(message = "available не может быть пустым.")
    private Boolean available;
    private UserDto owner;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<CommentResponseDto> comments;
    private Long requestId;
}
