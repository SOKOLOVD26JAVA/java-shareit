package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Headers;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                            @Valid @RequestBody BookingRequestDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto bookingApprove(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userID
            , @PathVariable Long bookingId, @RequestParam(required = true) Boolean approved) {
        return bookingService.bookingApprove(userID, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                         @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingByIdAndStatus(@RequestHeader(value = Headers.SHARER_USER_ID) Long userId
            , @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingByIdAndStatus(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingByOwnerAndStatus(@RequestHeader(value = Headers.SHARER_USER_ID) Long userId
            , @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingByOwnerAndStatus(userId, state);
    }
}
