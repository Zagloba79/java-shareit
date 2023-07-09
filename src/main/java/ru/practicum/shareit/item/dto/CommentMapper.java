package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto createCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(comment.getItem());
        commentDto.setAuthor(comment.getAuthor());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public static Comment createComment(CommentDto commentDto, Item item, User author) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        comment.setItem(commentDto.getItem());
        comment.setAuthor(commentDto.getAuthor());
        comment.setCreated(commentDto.getCreated());
        return comment;
    }
}
