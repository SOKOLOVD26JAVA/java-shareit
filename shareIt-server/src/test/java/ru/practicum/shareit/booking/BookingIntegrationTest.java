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
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserService userService;

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
        BookingResponseDto dto = bookingService.getBooking(user.getId(), savedBooking.getId());

        assertThat(dto.getEnd(), equalTo(bookingRequest.getEnd()));
        assertThat(dto.getStart(), equalTo(bookingRequest.getStart()));


        assertThatThrownBy(() -> bookingService.getBooking(userForTest.getId(), savedBooking.getId())).isInstanceOf(ValidationException.class);
    }

    @Test
    void getBookingByIdAndStatusAllTest() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByIdAndStatus(savedAnyUser.getId(), "ALL");

        assertThat(dtoList.size(), equalTo(1));


    }

    @Test
    void getBookingByIdAndStatusCurrentTest() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByIdAndStatus(savedAnyUser.getId(), "CURRENT");

        assertThat(dtoList.size(), equalTo(0));


    }

    @Test
    void getBookingByIdAndStatusPastTest() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByIdAndStatus(savedAnyUser.getId(), "PAST");

        assertThat(dtoList.size(), equalTo(0));

    }

    @Test
    void getBookingByIdAndStatusFutureTest() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByIdAndStatus(savedAnyUser.getId(), "FUTURE");

        assertThat(dtoList.size(), equalTo(1));

    }

    @Test
    void getBookingByIdAndStatusWaitingTest() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByIdAndStatus(savedAnyUser.getId(), "WAITING");

        assertThat(dtoList.size(), equalTo(1));

    }

    @Test
    void getBookingByIdAndStatusRejectedTest() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByIdAndStatus(savedAnyUser.getId(), "REJECTED");

        assertThat(dtoList.size(), equalTo(0));
    }

    @Test
    void getBookingByOwnerAndStatusAll() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByOwnerAndStatus(savedUser.getId(), "ALL");

        assertThat(dtoList.size(), equalTo(1));
    }

    @Test
    void getBookingByOwnerAndStatusCurrent() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByOwnerAndStatus(savedUser.getId(), "CURRENT");

        assertThat(dtoList.size(), equalTo(0));
    }

    @Test
    void getBookingByOwnerAndStatusPast() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByOwnerAndStatus(savedUser.getId(), "PAST");

        assertThat(dtoList.size(), equalTo(0));
    }

    @Test
    void getBookingByOwnerAndStatusFuture() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByOwnerAndStatus(savedUser.getId(), "FUTURE");

        assertThat(dtoList.size(), equalTo(1));
    }

    @Test
    void getBookingByOwnerAndStatusWaiting() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByOwnerAndStatus(savedUser.getId(), "WAITING");

        assertThat(dtoList.size(), equalTo(1));
    }

    @Test
    void getBookingByOwnerAndStatusRejected() {
        User user = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedUser = userService.createUser(UserMapper.mapUserDto(user));

        User anyuser = new User();
        user.setName("anotherUser");
        user.setEmail("another@email.com");
        UserDto savedAnyUser = userService.createUser(UserMapper.mapUserDto(anyuser));

        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(savedUser.getId(), request);

        BookingRequestDto bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(savedItem.getId());
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusHours(1));


        BookingResponseDto savedBooking = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        List<BookingResponseDto> dtoList = bookingService.getBookingByOwnerAndStatus(savedUser.getId(), "REJECTED");

        assertThat(dtoList.size(), equalTo(0));
    }

}
