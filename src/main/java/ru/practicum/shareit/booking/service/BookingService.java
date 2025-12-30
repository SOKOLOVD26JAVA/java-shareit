package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponseDto createBooking(Long userId, BookingRequestDto bookingRequestDto) {

        Item itemForBooking = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден."));
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));

        checkAvail(itemForBooking);
        checkStartEqEnd(bookingRequestDto);
        Booking booking = new Booking();

        booking.setItem(itemForBooking);
        booking.setBooker(booker);
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);


        return BookingMapper.mapToBookingResponseDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponseDto bookingApprove(Long userId, Long bookingId, Boolean approved) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BookingValidationException("Пользователь не найден."));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingValidationException("Бронирование не найдено."));
        if (!Objects.equals(booking.getItem().getOwner().getId(), user.getId())) {
            throw new BookingValidationException("Ошибка валидации пользователя.");
        }
        if (approved == true) {
            booking.setStatus(BookingStatus.APPROVED);
            Item item = booking.getItem();
            item.setAvailable(false);
            return BookingMapper.mapToBookingResponseDto(booking);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            return BookingMapper.mapToBookingResponseDto(booking);
        }
    }

    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingValidationException("Бронирование не найдено."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BookingValidationException("Пользователь не найден."));
        if (!Objects.equals(booking.getBooker().getId(), userId) || !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            return BookingMapper.mapToBookingResponseDto(booking);
        } else {
            throw new ValidationException("Ошибка валидации пользователя.");
        }
    }

    public List<BookingResponseDto> getBookingByIdAndStatus(Long userId, String stateString) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BookingValidationException("Пользователь не найден."));

        State state = State.valueOf(stateString.toUpperCase());
        switch (state) {
            case State.ALL -> {
                return bookingRepository.findByBookerIdOrderByStartDesc(userId).stream()
                        .map(BookingMapper::mapToBookingResponseDto).collect(Collectors.toList());
            }
            case State.CURRENT -> {
                return findByUserAndStatus(userId, BookingStatus.APPROVED);
            }
            case State.PAST -> {
                return findByUserAndStatus(userId, BookingStatus.CANCELED);

            }
            case State.FUTURE -> {
                return findByUserAndStatus(userId, BookingStatus.WAITING);
            }
            case State.WAITING -> {
                return findByUserAndStatus(userId, BookingStatus.WAITING);// Не понимаю какой тут нужен статус...
            }
            case State.REJECTED -> {
                return findByUserAndStatus(userId, BookingStatus.REJECTED);
            }
            default -> throw new IllegalArgumentException("Не корректный параметр запроса.");
        }

    }

    public List<BookingResponseDto> getBookingByOwnerAndStatus(Long userId, String stateString) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));

        State state = State.valueOf(stateString.toUpperCase());
        switch (state) {
            case State.ALL -> {
                List<Long> itemIds = itemRepository.findItemIds(userId);
                return bookingRepository.findByItemIds(itemIds).stream()
                        .map(BookingMapper::mapToBookingResponseDto).collect(Collectors.toList());
            }
            case State.CURRENT -> {
                return findByItemIdsAndStatus(userId, BookingStatus.APPROVED);
            }
            case State.PAST -> {
                return findByItemIdsAndStatus(userId, BookingStatus.CANCELED);
            }
            case State.FUTURE -> {
                return findByItemIdsAndStatus(userId, BookingStatus.WAITING);
            }
            case State.WAITING -> {
                return findByItemIdsAndStatus(userId, BookingStatus.WAITING);// Не понимаю какой тут нужен статус...
            }
            case State.REJECTED -> {
                return findByItemIdsAndStatus(userId, BookingStatus.REJECTED);
            }
            default -> throw new IllegalArgumentException("Не корректный параметр запроса.");
        }


    }

    private List<BookingResponseDto> findByItemIdsAndStatus(Long userId, BookingStatus status) {
        List<Long> itemIds = itemRepository.findItemIds(userId);
        return bookingRepository.findByItemIdsAndStatus(itemIds, status).stream()
                .map(BookingMapper::mapToBookingResponseDto).collect(Collectors.toList());
    }

    private List<BookingResponseDto> findByUserAndStatus(Long userId, BookingStatus status) {
        return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, status).stream()
                .map(BookingMapper::mapToBookingResponseDto).collect(Collectors.toList());
    }

    private void checkAvail(Item item) {
        if (item.getAvailable() == false) {
            throw new BookingValidationException("Предмет не доступен для бронирования.");
        } else {
            return;
        }
    }

    private void checkStartEqEnd(BookingRequestDto booking) {
        if (booking.getStart().equals(booking.getEnd())) {
            throw new BookingValidationException("Время начала бронирования не должно совпадать с временем окончания");
        } else {
            return;
        }
    }


}
