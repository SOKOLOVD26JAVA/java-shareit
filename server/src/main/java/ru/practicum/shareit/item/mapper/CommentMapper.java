package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static Comment mapToComment(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();

        comment.setId(commentRequestDto.getId());
        comment.setText(commentRequestDto.getText());
        comment.setAuthor(commentRequestDto.getAuthor());
        comment.setItem(commentRequestDto.getItem());
        comment.setCreated(commentRequestDto.getCreated());

        return comment;
    }

    public static CommentResponseDto mapToCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setAuthorName(comment.getAuthor().getName());
        commentResponseDto.setCreated(comment.getCreated());

        return commentResponseDto;

    }
}
