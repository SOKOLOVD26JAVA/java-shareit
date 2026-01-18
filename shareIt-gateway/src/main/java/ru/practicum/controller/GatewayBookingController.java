package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.client.ShareItBookingClient;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class GatewayBookingController {
    private final ShareItBookingClient shareItClient;

    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                            @Valid @RequestBody BookingRequestDto bookingDto) {
        return shareItClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto bookingApprove(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userID,
                                             @PathVariable Long bookingId, @RequestParam(required = true) Boolean approved) {
        return shareItClient.bookingApprove(userID, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                         @PathVariable Long bookingId) {
        return shareItClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getBookingByIdAndStatus(@RequestHeader(value = Headers.SHARER_USER_ID) Long userId,
                                                            @RequestParam(defaultValue = "ALL") String state) {
        return shareItClient.getBookingByIdAndStatus(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingByOwnerAndStatus(@RequestHeader(value = Headers.SHARER_USER_ID) Long userId,
                                                               @RequestParam(defaultValue = "ALL") String state) {
        return shareItClient.getBookingByOwnerAndStatus(userId, state);
    }
}
