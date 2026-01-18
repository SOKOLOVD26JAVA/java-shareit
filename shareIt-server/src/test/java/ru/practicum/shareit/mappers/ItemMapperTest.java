package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void mapToItemDtoAvailableTest() {
        User owner = new User();
        owner.setId(1L);
        owner.setName("Дима");
        owner.setEmail("owner@email.ru");

        ItemRequest request = new ItemRequest();
        request.setId(99L);

        Comment comment = new Comment();
        comment.setId(50L);
        comment.setText("Хорошая вещь");
        comment.setAuthor(owner);
        comment.setCreated(LocalDateTime.now());

        Item item = new Item();
        item.setId(100L);
        item.setName("Молоток");
        item.setDescription("Красивый молоток");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);
        item.setComments(List.of(comment));

        ItemDto result = ItemMapper.mapToItemDto(item);

        assertEquals(100L, result.getId());
        assertEquals("Молоток", result.getName());
        assertEquals("Красивый молоток", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(1L, result.getOwner().getId());
        assertEquals(99L, result.getRequestId());
        assertNotNull(result.getComments());
        assertEquals(1, result.getComments().size());
        assertEquals("Норм вещь", result.getComments().get(0).getText());
    }

    @Test
    void mapToItemDtoToNullCommentsTest() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(100L);
        item.setName("Молоток");
        item.setDescription("Красивый молоток");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setComments(null);

        ItemDto result = ItemMapper.mapToItemDto(item);

        assertNull(result.getComments());
    }

    @Test
    void mapToItemDtoToNullRequestIdTest() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(100L);
        item.setName("Молоток");
        item.setDescription("Красивый молоток");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(null);

        ItemDto result = ItemMapper.mapToItemDto(item);

        assertNull(result.getRequestId());
    }

    @Test
    void mapToItemToOwnerNullTest() {
        ItemDto dto = new ItemDto();
        dto.setId(200L);
        dto.setName("Молоток");
        dto.setDescription("Красивый молоток");
        dto.setAvailable(true);

        Item result = ItemMapper.mapToItem(dto);

        assertEquals(200L, result.getId());
        assertEquals("Молоток", result.getName());
        assertEquals("Красивый молоток", result.getDescription());
        assertTrue(result.getAvailable());
        assertNull(result.getOwner());
    }

    @Test
    void mapToItemTestToAvailableTest() {
        ItemDto dto = new ItemDto();
        dto.setId(null);
        dto.setName("Молоток");
        dto.setDescription("Красивый молоток");
        dto.setAvailable(true);

        Item result = ItemMapper.mapToItem(dto);

        assertNull(result.getId());
        assertEquals("Молоток", result.getName());
        assertEquals("Красивый молоток", result.getDescription());
        assertTrue(result.getAvailable());
    }

    @Test
    void mapToItemForUserDtoTest() {
        Item item = new Item();
        item.setName("Молоток");
        item.setDescription("Красивый молоток");

        ItemForUserDto result = ItemMapper.mapToItemForUserDto(item);

        assertEquals("Молоток", result.getName());
        assertEquals("Красивый молоток", result.getDescription());
    }

    @Test
    void mapToItemForRequestDtoTest() {
        User owner = new User();
        owner.setId(777L);

        Item item = new Item();
        item.setId(888L);
        item.setName("Молоток");
        item.setOwner(owner);

        ItemForRequestDto result = ItemMapper.mapToItemForRequestDto(item);

        assertEquals(888L, result.getId());
        assertEquals("Молоток", result.getName());
        assertEquals(777L, result.getOwnerId());
    }

}
