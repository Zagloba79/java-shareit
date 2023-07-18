package ru.practicum.shareit.item;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingForDataDto;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handler.EntityHandler;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithCommentsAndBookingsDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final EntityHandler handler;
    private final ItemRepository itemRepository;
    private final BookingService bookingService;
    private final CommentRepository commentRepository;
    static Predicate<Item> isAvailable = item ->
            Boolean.TRUE.equals(item.getAvailable());

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        handler.itemValidate(itemDto);
        User owner = handler.getUserFromOpt(ownerId);
        Item item = ItemMapper.createItem(itemDto, owner);
        itemRepository.save(item);
        return ItemMapper.createItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Item item = handler.getItemFromOpt(itemId);
        User owner = handler.getUserFromOpt(ownerId);
        if (!item.getOwner().equals(owner)) {
            throw new ObjectNotFoundException("Собственник не тот");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        return ItemMapper.createItemDto(item);
    }

    @Override
    public ItemWithCommentsAndBookingsDto getItemById(Long itemId, Long ownerId) {
        User user = handler.getUserFromOpt(ownerId);
        Item item = handler.getItemFromOpt(itemId);
        ItemWithCommentsAndBookingsDto itemWithCommentsAndBookingsDto =
                ItemMapper.createItemWithCommentsAndBookingsDto(item);
        addCommentsToItem(itemWithCommentsAndBookingsDto);
        if (item.getOwner().getId().equals(user.getId())) {
            addBookingsDataToItem(itemWithCommentsAndBookingsDto);
        }
        return itemWithCommentsAndBookingsDto;
    }

    @Override
    public List<ItemWithCommentsAndBookingsDto> getItemsByOwner(Long ownerId) {
        User owner = handler.getUserFromOpt(ownerId);
        List<Item> itemsByOwner = itemRepository.findByOwnerId(owner.getId());
        List<ItemWithCommentsAndBookingsDto> convertedItems = itemsByOwner.stream()
                .map(ItemMapper::createItemWithCommentsAndBookingsDto)
                .sorted(Comparator.comparing(ItemWithCommentsAndBookingsDto::getId))
                .collect(toList());
        List<ItemWithCommentsAndBookingsDto> itemsWithCommentsAndBookings =
                new ArrayList<>();
        for (ItemWithCommentsAndBookingsDto itemWithCommentsAndBookingsDto : convertedItems) {
            addCommentsToItem(itemWithCommentsAndBookingsDto);
            addBookingsDataToItem(itemWithCommentsAndBookingsDto);
            itemsWithCommentsAndBookings.add(itemWithCommentsAndBookingsDto);
        }

        return itemsWithCommentsAndBookings;
    }

    private void addCommentsToItem(ItemWithCommentsAndBookingsDto item) {
        List<Comment> comments = commentRepository.findByItemId(item.getId());
        if (comments != null) {
            item.setComments(comments.stream()
                    .map(CommentMapper::createCommentDto)
                    .collect(toList()));
        }
    }

    private void addBookingsDataToItem(ItemWithCommentsAndBookingsDto item) {
        Long itemId = item.getId();
        BookingForDataDto previousBooking = bookingService.getLastBooking(itemId);
        if (previousBooking != null) {
            item.setLastBooking(previousBooking);
        }
        BookingForDataDto nextBooking = bookingService.getNextBooking(itemId);
        if (nextBooking != null) {
            item.setNextBooking(nextBooking);
        }
    }

    @Override
    public List<ItemDto> findAll() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::createItemDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(toList());
    }

    @Override
    public void deleteItem(Long itemId, Long ownerId) {
        Item item = handler.getItemFromOpt(itemId);
        User owner = handler.getUserFromOpt(ownerId);
        if (item.getOwner().getId().equals(owner.getId())) {
            itemRepository.deleteById(itemId);
        }
    }

    @Override
    public List<ItemDto> getItemsByQuery(String text, Long ownerId) {
        User owner = handler.getUserFromOpt(ownerId);
        if ((text != null) && (!text.isBlank())) {
            String lowerText = text.toLowerCase();
            return itemRepository.getItemsByQuery(lowerText).stream()
                    .filter(item -> isAvailable.test(item))
                    .map(ItemMapper::createItemDto)
                    .collect(toList());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long authorId, Long itemId) {
        if (commentDto.getText().isBlank()) {
            throw new ValidationException("Пустой текст");
        }
        Item item = handler.getItemFromOpt(itemId);
        User author = handler.getUserFromOpt(authorId);
        handler.authorValidate(authorId, itemId);
        Comment comment = CommentMapper.createNewComment(commentDto);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthor(author);
        commentRepository.save(comment);
        return CommentMapper.createCommentDto(comment);
    }
}