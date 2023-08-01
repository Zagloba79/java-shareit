package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommentMapperTest {

    @Test
    public void createCommentDtoTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        User author = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        Item item = new Item("name", "desc", true, owner, null);
        Comment comment = new Comment(1L, "tekst", item, author, LocalDateTime.now());
        CommentDto commentDto = CommentMapper.createCommentDto(comment);
        assertEquals(1L, commentDto.getId());
        assertEquals("tekst", commentDto.getText());
        assertEquals("ann", commentDto.getAuthorName());
    }

    @Test
    public void createCommentTest() {
        User author = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        CommentDto commentDto = new CommentDto("tekst", author.getName(), LocalDateTime.now());
        Comment comment = CommentMapper.createComment(commentDto);
        assertEquals("tekst", comment.getText());
        assertNull(comment.getItem());
        assertNull(comment.getAuthor());
        comment.setAuthor(author);
        assertEquals(author, comment.getAuthor());
    }

    @Test
    public void createNewCommentTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        User author = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        Item item = new Item("name", "desc", true, owner, null);
        CommentDto commentDto = new CommentDto("text", author.getName(), LocalDateTime.now());
        Comment comment = CommentMapper.createNewComment(commentDto, item, author);
        assertEquals("text", comment.getText());
        assertEquals(author, comment.getAuthor());
        assertEquals(item, comment.getItem());
    }
}
