package ru.practicum.shareit.jsonTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingStatus;
import ru.practicum.item.dto.CommentRequestDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.itemRequest.dto.ItemRequestWithOutResponseDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@org.springframework.boot.test.autoconfigure.json.JsonTest
public class JsonTest {
    @Autowired
    private JacksonTester<UserDto> userJson;
    @Autowired
    private JacksonTester<ItemDto> itemJson;
    @Autowired
    private JacksonTester<ItemRequestWithOutResponseDto> itemRequestJson;
    @Autowired
    private JacksonTester<BookingRequestDto> bookingJson;
    @Autowired
    private JacksonTester<CommentRequestDto> commentJson;

    @Test
    void itemDtoTest() throws Exception {
        UserDto owner = new UserDto();
        owner.setId(1L);
        owner.setName("Дима");
        owner.setEmail("user@email.com");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Молоток");
        itemDto.setDescription("Красивый молоток");
        itemDto.setAvailable(true);
        itemDto.setOwner(owner);
        itemDto.setLastBooking(LocalDateTime.of(2023, 1, 1, 10, 0));
        itemDto.setNextBooking(LocalDateTime.of(2023, 1, 2, 10, 0));
        itemDto.setRequestId(100L);


        JsonContent<ItemDto> result = itemJson.write(itemDto);


        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Молоток");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Красивый молоток");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("Дима");
        assertThat(result).extractingJsonPathStringValue("$.lastBooking")
                .isEqualTo("2023-01-01T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking")
                .isEqualTo("2023-01-02T10:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(100);
    }

    @Test
    void userDtoTest() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Дима");
        userDto.setEmail("user@email.com");

        JsonContent<UserDto> result = userJson.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дима");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("user@email.com");

    }

    @Test
    void itemRequestDtoTest() throws Exception {
        ItemRequestWithOutResponseDto dto = new ItemRequestWithOutResponseDto();
        dto.setId(1L);
        dto.setDescription("Нужен молоток КРАСИВЫЙ!");
        dto.setCreated(LocalDateTime.of(2023, 1, 1, 10, 30, 0));
        dto.setRequesterId(5L);

        JsonContent<ItemRequestWithOutResponseDto> result = itemRequestJson.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Нужен молоток КРАСИВЫЙ!");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2023-01-01T10:30:00");
        assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(5);
    }

    @Test
    void bookingRequestDtoTest() throws Exception {
        ItemDto item = new ItemDto();
        item.setId(10L);
        item.setName("Молоток");

        UserDto booker = new UserDto();
        booker.setId(20L);
        booker.setName("Дима");

        BookingRequestDto dto = new BookingRequestDto();
        dto.setId(1L);
        dto.setStart(LocalDateTime.of(2023, 1, 1, 10, 0));
        dto.setEnd(LocalDateTime.of(2023, 1, 2, 10, 0));
        dto.setItemId(10L);
        dto.setItem(item);
        dto.setBooker(booker);
        dto.setStatus(BookingStatus.WAITING);

        JsonContent<BookingRequestDto> result = bookingJson.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo("2023-01-01T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo("2023-01-02T10:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(10);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(10);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Молоток");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(20);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("Дима");
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

    @Test
    void commentRequestDtoTest() throws Exception {
        UserDto author = new UserDto();
        author.setId(1L);
        author.setName("Дима");
        author.setEmail("user@email.com");

        ItemDto item = new ItemDto();
        item.setId(10L);
        item.setName("Молоток");
        item.setDescription("Красивый молоток");
        item.setAvailable(true);

        CommentRequestDto dto = new CommentRequestDto();
        dto.setId(100L);
        dto.setText("Молоток реально красивый))");
        dto.setAuthor(author);
        dto.setItem(item);
        dto.setCreated(LocalDateTime.of(2023, 6, 15, 14, 30, 0));

        JsonContent<CommentRequestDto> result = commentJson.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(100);
        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo("Молоток реально красивый))");
        assertThat(result).extractingJsonPathNumberValue("$.author.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.author.name").isEqualTo("Дима");
        assertThat(result).extractingJsonPathStringValue("$.author.email")
                .isEqualTo("user@email.com");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(10);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Молоток");
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("Красивый молоток");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2023-06-15T14:30:00");
    }
}
