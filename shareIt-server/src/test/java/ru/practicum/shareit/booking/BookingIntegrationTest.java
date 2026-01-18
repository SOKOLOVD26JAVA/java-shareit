package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BookingIntegrationTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    BookingService bookingService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    private User userForTest;
    private ItemDto request;
    private BookingRequestDto bookingRequest;

    @BeforeEach
    void setUpUserAndItem() {
        userRepository.deleteAll();
        itemRepository.deleteAll();

        userForTest = new User();
        userForTest.setName("Юзер");
        userForTest.setEmail("email@yandex.ru");
        userForTest = userRepository.save(userForTest);

        request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        request = itemService.createItem(userForTest.getId(), request);
    }

    @BeforeEach
    void setUpBooking() {
        bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(request.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));
    }

    @Test
    void createBookingTest() {
        BookingResponseDto dto = bookingService.createBooking(userForTest.getId(), bookingRequest);

        assertThat(dto.getId(), notNullValue());
        assertThat(dto.getBooker().getId(), notNullValue());
        assertThat(dto.getStatus(), equalTo(BookingStatus.WAITING));
    }

    @Test
    void approveBookingTest() {
        BookingResponseDto savedBooking = bookingService.createBooking(userForTest.getId(), bookingRequest);
        BookingResponseDto dto = bookingService.bookingApprove(userForTest.getId(), savedBooking.getId(), true);
        assertThat(dto.getStatus(), equalTo(BookingStatus.APPROVED));
        BookingResponseDto dto1 = bookingService.bookingApprove(userForTest.getId(), savedBooking.getId(), false);
        assertThat(dto1.getStatus(), equalTo(BookingStatus.REJECTED));
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        userRepository.save(user);

        assertThatThrownBy(() -> bookingService.bookingApprove(user.getId(), savedBooking.getId(), true)).isInstanceOf(BookingValidationException.class);
    }

    @Test
    void getBookingTest() {

        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        userRepository.save(user);

        BookingResponseDto savedBooking = bookingService.createBooking(userForTest.getId(), bookingRequest);
        BookingResponseDto dto = bookingService.getBooking(user.getId(),savedBooking.getId());

        assertThat(dto.getEnd(),equalTo(bookingRequest.getEnd()));
        assertThat(dto.getStart(),equalTo(bookingRequest.getStart()));




        assertThatThrownBy(() -> bookingService.getBooking(userForTest.getId(), savedBooking.getId())).isInstanceOf(ValidationException.class);

    }


}
