package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithOutResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
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
@Transactional
@ActiveProfiles("test")
public class ItemRequestIntegrationTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    BookingService bookingService;

    private User userForTest;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUpUser() {
        userRepository.deleteAll();

        userForTest = new User();
        userForTest.setName("Юзер");
        userForTest.setEmail("email@yandex.ru");
        userForTest = userRepository.save(userForTest);
    }


    @Test
    void createItemRequestTest() {
        requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужен молоток.");
        ItemRequestWithOutResponseDto createdRequest = itemRequestService.createItemRequest(userForTest.getId(), requestDto);

        assertThat(createdRequest.getId(), notNullValue());
        assertThat(createdRequest.getRequesterId(), equalTo(userForTest.getId()));
    }

    @Test
    void getItemRequestTest() {
        requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужен молоток.");
        ItemRequestWithOutResponseDto createdRequest = itemRequestService.createItemRequest(userForTest.getId(), requestDto);

        itemRequestService.getItemRequestById(userForTest.getId(), createdRequest.getId());

        assertThat(createdRequest.getId(), notNullValue());
        assertThat(createdRequest.getRequesterId(), equalTo(userForTest.getId()));
    }

    @Test
    void createItemOnRequestTest() {
        requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужен молоток.");
        ItemRequestWithOutResponseDto createdRequest = itemRequestService.createItemRequest(userForTest.getId(), requestDto);

        User anotherUser = new User();
        anotherUser.setName("anotherUser");
        anotherUser.setEmail("another@email.com");
        userRepository.save(anotherUser);

        ItemDto item = new ItemDto();
        item.setName("молоток");
        item.setDescription("красивый");
        item.setAvailable(true);
        item.setRequestId(createdRequest.getId());

        ItemDto createdItemOnRequest = itemService.createItem(anotherUser.getId(), item);

        assertThat(createdItemOnRequest.getRequestId(), equalTo(createdRequest.getId()));
    }

    @Test
    void createCommentTest() {
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
        bookingRequest.setEnd(LocalDateTime.now().minusMinutes(1));
        BookingResponseDto bookingResponseDto = bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        bookingService.bookingApprove(savedUser.getId(), bookingResponseDto.getId(), true);
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Отличный молоток.");
        CommentResponseDto commentResponseDto = itemService.createComment(savedAnyUser.getId(), savedItem.getId(), commentRequestDto);

        assertThat(commentResponseDto.getId(), notNullValue());
    }

    @Test
    void getItemTest() {
        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        ItemDto savedItem = itemService.createItem(userForTest.getId(), request);

        ItemDto findItem = itemService.getItem(savedItem.getId());

        assertThat(findItem.getName(), equalTo(request.getName()));
        assertThat(findItem.getDescription(), equalTo(request.getDescription()));
    }

    @Test
    void getItemListByUserId() {
        ItemDto request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
        itemService.createItem(userForTest.getId(), request);

        List<ItemForUserDto> items = itemService.getItemListByUserId(userForTest.getId());

        assertThat(items.size(),equalTo(1));
    }

    @Test
    void createCommentNotValidTest() {
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
        bookingRequest.setEnd(LocalDateTime.now().plusMinutes(1));
        bookingService.createBooking(savedAnyUser.getId(), bookingRequest);

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Отличный молоток.");

        assertThatThrownBy(() -> itemService.createComment(savedAnyUser.getId(), savedItem.getId(), commentRequestDto)).isInstanceOf(BookingValidationException.class);
    }



}
