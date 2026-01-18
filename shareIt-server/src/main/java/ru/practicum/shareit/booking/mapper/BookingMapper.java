package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {

    public static Booking mapToBooking(BookingRequestDto bookingRequestDto) {
        Booking booking = new Booking();

        booking.setId(bookingRequestDto.getId());
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setItem(ItemMapper.mapToItem(bookingRequestDto.getItem()));
        booking.setBooker(UserMapper.mapToUser(bookingRequestDto.getBooker()));
        booking.setStatus(bookingRequestDto.getStatus());

        return booking;
    }

    public static BookingResponseDto mapToBookingResponseDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();

        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setItem(ItemMapper.mapToItemDto(booking.getItem()));
        bookingResponseDto.setBooker(UserMapper.mapUserDto(booking.getBooker()));
        bookingResponseDto.setStatus(booking.getStatus());

        return bookingResponseDto;
    }
}
