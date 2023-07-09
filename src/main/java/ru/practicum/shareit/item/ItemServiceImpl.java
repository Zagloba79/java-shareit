package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handler.OptionalHandler;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final OptionalHandler optionalHandler;
    private final ItemRepository itemRepository;
    private final ItemRequestStorage itemRequestStorage;
    private final BookingService bookingService;
    private final CommentRepository commentRepository;

    static Predicate<Item> isAvailable = item ->
            Boolean.TRUE.equals(item.getAvailable());
    static BiPredicate<Item, String> isMatch = (item, text) -> (item.getName() != null &&
            item.getName().toLowerCase().contains(text)) ||
            (item.getDescription() != null &&
                    item.getDescription().toLowerCase().contains(text));

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = optionalHandler.getUserFromOpt(ownerId);
        Item item = ItemMapper.createItem(itemDto, owner);
        itemValidate(item);
        Optional<ItemRequest> request = itemRequestStorage.getItemRequestByItem(item);
        request.ifPresent(item::setRequest);
        itemRepository.save(item);
        return ItemMapper.createItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Item item = optionalHandler.getItemFromOpt(itemId);
        User owner = optionalHandler.getUserFromOpt(ownerId);
        if (!item.getOwner().equals(owner)) {
            throw new ObjectNotFoundException("Собственник не тот");
        }
        Item itemToUpdate = ItemMapper.createItem(itemDto, owner);
        if (itemToUpdate.getName() != null) {
            item.setName(itemToUpdate.getName());
        }
        if (itemToUpdate.getDescription() != null) {
            item.setDescription(itemToUpdate.getDescription());
        }
        if (itemToUpdate.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        return ItemMapper.createItemDto(itemToUpdate);
    }

    @Override
    public ItemWithCommentsDto getItemById(Long itemId, Long ownerId) {
        User owner = optionalHandler.getUserFromOpt(ownerId);
        Item item = optionalHandler.getItemFromOpt(itemId);
        List<Comment> comments = getCommentsByItem(itemId);
        return ItemMapper.createItemWithCommentsDto(item, comments);
    }

    @Override
    public List<ItemWithDatesAndCommentsDto> getItemsByOwner(Long ownerId) {
        User owner = optionalHandler.getUserFromOpt(ownerId);
        List<ItemDto> itemsByOwner = itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .map(ItemMapper::createItemDto)
                .collect(toList());
        List<ItemWithDatesAndCommentsDto> itemsWithDatesAndComments = new ArrayList<>();
        for (ItemDto itemDto : itemsByOwner) {
            ItemWithDatesAndCommentsDto item = new ItemWithDatesAndCommentsDto();
            item.setId(itemDto.getId());
            item.setName(itemDto.getName());
            item.setDescription(itemDto.getDescription());
            item.setAvailable(itemDto.getAvailable());
            item.setDates(bookingService.getBookingsBeforeAndAfterNow(itemDto.getId(), ownerId));
            item.setComments(getCommentsByItem(item.getId()));
            itemsWithDatesAndComments.add(item);
        }
        return itemsWithDatesAndComments;
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
        Item item = optionalHandler.getItemFromOpt(itemId);
        User owner = optionalHandler.getUserFromOpt(ownerId);
        if (item.getOwner().getId().equals(owner.getId())) {
            itemRepository.deleteById(itemId);
        }
    }

    @Override
    public List<ItemDto> getItemsByQuery(String text, Long ownerId) {
        User owner = optionalHandler.getUserFromOpt(ownerId);
        if ((text != null) && (!text.isBlank())) {
            String lowerText = text.toLowerCase();
            return itemRepository.findAll().stream()
                    .filter(item -> isAvailable.test(item) && (isMatch.test(item, lowerText)))
                    .map(ItemMapper::createItemDto)
                    .sorted(Comparator.comparing(ItemDto::getId))
                    .collect(toList());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long authorId) {
        Item item = optionalHandler.getItemFromOpt(itemId);
        User author = optionalHandler.getUserFromOpt(authorId);
        authorValidate(itemId, authorId);
        Comment comment = CommentMapper.createComment(commentDto);
        commentRepository.save(comment);
        return CommentMapper.createCommentDto(comment);
    }

    private List<Comment> getCommentsByItem(Long itemId) {
        return commentRepository.findAll().stream()
                .filter(comment -> comment.getItem().getId().equals(itemId))
                .collect(toList());
    }

    private void itemValidate(Item item) {
        if (item.getAvailable() == null) {
            throw new ValidationException("Поле статуса не заполнено");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Некорректное название предмета: " + item.getName());
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Некорректное описание предмета: " + item.getDescription());
        }
    }

    private void authorValidate(Long itemId, Long authorId) {
        if (bookingService.getBookingsDtoByItem(itemId, authorId).stream()
                .filter(bookingDto -> bookingDto.getBooker().getId().equals(authorId))
                .noneMatch(bookingDto -> bookingDto.getStatus().equals(APPROVED))) {
            throw new ValidationException(
                    "Юзер с id = " + authorId + " никогда не арендовал предмет с id = " + itemId);
        }
    }
}