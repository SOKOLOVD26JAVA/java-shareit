package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;


import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingRequestDto {
    private Long id;
    @NotNull(message = "Дата начала обязательна.")
//    @FutureOrPresent(message = "Дата начала должна быть в настоящем или будущем")
    private LocalDateTime start;
    @NotNull(message = "Дата окончания обязательна.")
    @Future(message = "Дата окончания должна быть в будущем")
    private LocalDateTime end;
    private ItemDto item;
    private Long itemId;
    private UserDto booker;
    private BookingStatus status;
}
