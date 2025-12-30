package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    @NotBlank(message = "Описание не может быть пустым.")
    @Size(max = 200, message = "Описание не более 200 символов.")
    private String description;
    @NotNull(message = "available не может быть пустым.")
    private Boolean available;
    UserDto owner;
    LocalDateTime lastBooking = null;
    LocalDateTime nextBooking = null;
    List<CommentResponseDto> comments;

}
