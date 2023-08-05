package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto createCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated());
        commentDto.setAuthorName(comment.getAuthor().getName());
        return commentDto;
    }

    public static Comment createComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        if (commentDto.getCreated() != null) {
            comment.setCreated(commentDto.getCreated());
        }
        return comment;
    }

    public static Comment createNewComment(CommentDto commentDto, Item item, User author) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setText(commentDto.getText());
        if (commentDto.getCreated() != null) {
            comment.setCreated(commentDto.getCreated());
        }
        return comment;
    }
}