package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CommentMapperTest {

    @Test
    void mapToCommentTest() {
        CommentRequestDto commentDto = new CommentRequestDto();

        User user = new User();
        user.setName("Дима");
        user.setEmail("super@email.ru");

        Item item = new Item();
        item.setId(100L);
        item.setName("Молоток");
        item.setDescription("Красивый молоток");
        item.setAvailable(true);
        item.setOwner(user);

        commentDto.setId(666L);
        commentDto.setAuthor(user);
        commentDto.setItem(item);
        commentDto.setText("Клевый молоток");
        commentDto.setCreated(LocalDateTime.now());

        Comment comment = CommentMapper.mapToComment(commentDto);

        assertEquals("Дима", comment.getAuthor().getName());
        assertEquals("super@email.ru", comment.getAuthor().getEmail());
        assertEquals("Молоток", comment.getItem().getName());
        assertEquals("Красивый молоток", comment.getItem().getDescription());
    }
}
