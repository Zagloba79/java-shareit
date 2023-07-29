package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.OperationIsNotSupported;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsAndBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceITTest {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final EntityHandler handler;

    LocalDateTime presentTime = LocalDateTime.now();
    private UserDto ownerDto = new UserDto("owner", "ooo@user.ru");
    private UserDto userDto = new UserDto("notOwner", "nnn@user.ru");
    private ItemDto itemDto = new ItemDto("item", "Descqwqw", true, null);
    private CommentDto commentDto = new CommentDto("comment", "notOwner", presentTime);
    private final NewBookingDto newbookingDto = new NewBookingDto(LocalDateTime.now().plusSeconds(1),
            LocalDateTime.now().plusSeconds(2),
            itemDto.getId());

    @Test
    @DirtiesContext
    public void shouldGetItemByIdNotOwnerTest() {
        ownerDto = userService.create(ownerDto);
        UserDto bookerDto = userService.create(userDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        newbookingDto.setItemId(itemDto.getId());
        BookingDto bookingDto = bookingService.create(newbookingDto, bookerDto.getId());
        bookingService.update(bookingDto.getId(), ownerDto.getId(), true);
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        commentDto = itemService.createComment(commentDto, bookerDto.getId(), itemDto.getId());
        Comment comment = CommentMapper.createComment(commentDto);
        ItemWithCommentsAndBookingsDto itemWithComments = itemService.getItemById(
                itemDto.getId(), bookerDto.getId());
        assertEquals(1, itemWithComments.getId());
        assertNull(itemWithComments.getLastBooking());
        assertNull(itemWithComments.getNextBooking());
        assertEquals(1, comment.getId());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenNotOwnerDeleteItemTest() {
        ownerDto = userService.create(ownerDto);
        userDto = userService.create(userDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        OperationIsNotSupported exception = assertThrows(OperationIsNotSupported.class,
                () -> itemService.deleteItem(itemDto.getId(), userDto.getId()));
        assertEquals("You are not owner", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenItemNotValidatedTest() {
        ownerDto = userService.create(ownerDto);
        ItemDto itemDto = new ItemDto(null, null, null, null);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.create(itemDto, ownerDto.getId()));
        assertEquals("Некорректное название предмета", exception.getMessage());
        itemDto.setName("name");
        exception = assertThrows(ValidationException.class,
                () -> itemService.create(itemDto, ownerDto.getId()));
        assertEquals("Некорректное описание предмета", exception.getMessage());
        itemDto.setDescription("Desc");
        exception = assertThrows(ValidationException.class,
                () -> itemService.create(itemDto, ownerDto.getId()));
        assertEquals("Некорректный статус предмета", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void deleteItemTest() {
        ownerDto = userService.create(userDto);
        ItemDto itemFromDb = itemService.create(itemDto, ownerDto.getId());
        assertEquals(itemDto.getName(), itemFromDb.getName());
        itemService.deleteItem(itemFromDb.getId(), ownerDto.getId());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> itemService.getItemById(itemFromDb.getId(), ownerDto.getId()));
        assertEquals("Предмета с " + itemFromDb.getId() + " не существует.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenWrongItemIdTest() {
        Long itemId = 210L;
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> handler.getItemFromOpt(itemId));
        assertEquals("Предмета с " + itemId + " не существует.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void didNotUpdateItemTest() {
        ownerDto = userService.create(ownerDto);
        ItemDto savedItem = itemService.create(itemDto, ownerDto.getId());
        ItemDto itemToUpdate = new ItemDto(null, null, null, null);
        ItemDto updatedItemDto = itemService.updateItem(itemToUpdate, savedItem.getId(), ownerDto.getId());
        assertEquals(updatedItemDto.getName(), savedItem.getName());
        assertEquals(updatedItemDto.getDescription(), savedItem.getDescription());
        assertEquals(updatedItemDto.getAvailable(), savedItem.getAvailable());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenNotOwnerUpdateItemTest() {
        ownerDto = userService.create(ownerDto);
        userDto = userService.create(userDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        ItemDto newItem = new ItemDto("mmm", "Desc", true, null);
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> itemService.updateItem(newItem, itemDto.getId(), userDto.getId()));
        assertEquals("Собственник не тот", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void shouldReturnItemsByOwnerTest() {
        ownerDto = userService.create(ownerDto);
        itemService.create(itemDto, ownerDto.getId());
        ItemDto secDto = new ItemDto("hhh", "dsda", true, null);
        itemService.create(secDto, ownerDto.getId());
        List<ItemWithCommentsAndBookingsDto> listItems =
                itemService.getItemsByOwnerPageable(ownerDto.getId(), 0, 10);
        assertEquals(2, listItems.size());
    }

    @Test
    @DirtiesContext
    public void shouldReturnItemsByQuery() {
        ownerDto = userService.create(ownerDto);
        itemService.create(itemDto, ownerDto.getId());
        ItemDto secDto = new ItemDto("bvbv", "dsda", true, null);
        itemService.create(secDto, ownerDto.getId());
        List<ItemDto> listItems = itemService.getItemsByQueryPageable(
                0, 10, "item", ownerDto.getId());
        assertEquals(1, listItems.size());
        ItemDto thDto = new ItemDto("bvbv", "itemDesc", true, null);
        itemService.create(thDto, ownerDto.getId());
        listItems = itemService.getItemsByQueryPageable(
                0, 10, "item", ownerDto.getId());
        assertEquals(2, listItems.size());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenNotBookerCreateComment() {
        ownerDto = userService.create(ownerDto);
        userDto = userService.create(userDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        LocalDateTime presentTime = LocalDateTime.of(2023, 1, 2,
                3, 4, 5);
        CommentDto commentDto = new CommentDto("Comment", ownerDto.getName(), presentTime);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.createComment(commentDto, userDto.getId(), itemDto.getId()));
        assertEquals("Юзер с id = " + userDto.getId() +
                " ещё не арендовал предмет с id = " + itemDto.getId(), exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void shouldEmptyListWhenQueryIsNull() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        List<ItemDto> itemsByQuery = itemService.getItemsByQueryPageable(
                0, 10, null, ownerDto.getId());
        assertEquals(Collections.EMPTY_LIST, itemsByQuery);
    }

    @Test
    @DirtiesContext
    public void shouldEmptyListWhenQueryIsBlank() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        List<ItemDto> itemsByQuery = itemService.getItemsByQueryPageable(
                0, 10, "     ", ownerDto.getId());
        assertEquals(Collections.EMPTY_LIST, itemsByQuery);
    }

    @Test
    @DirtiesContext
    public void exceptionWhenCommentIsBlank() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        LocalDateTime presentTime = LocalDateTime.of(2023, 1, 2,
                3, 4, 5);
        CommentDto commentDto = new CommentDto(" ", ownerDto.getName(), presentTime);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.createComment(commentDto, ownerDto.getId(), itemDto.getId()));
        assertEquals("Пустой текст", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void shouldCreateComment() {
        ownerDto = userService.create(ownerDto);
        userDto = userService.create(userDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(3),
                itemDto.getId()
        );
        BookingDto bookingDto = bookingService.create(newBookingDto, userDto.getId());
        bookingService.update(bookingDto.getId(), ownerDto.getId(), true);
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        CommentDto commentDto = new CommentDto("Comment", userDto.getName(), LocalDateTime.now());
        itemService.createComment(commentDto, userDto.getId(), itemDto.getId());
        List<ItemWithCommentsAndBookingsDto> itemsWithComments = itemService.getItemsByOwnerPageable(
                ownerDto.getId(), 0, 10);
        List<CommentDto> comments = new ArrayList<>();
        for (ItemWithCommentsAndBookingsDto item : itemsWithComments) {
            comments.addAll(item.getComments());
        }
        assertEquals(1, comments.size());
    }
}