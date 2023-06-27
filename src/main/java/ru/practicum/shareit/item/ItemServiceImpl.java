package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.feedback.FeedbackService;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;
    private final FeedbackService feedbackService;
    private final ItemRequestStorage itemRequestStorage;
    private Integer itemId = 1;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, ItemMapper itemMapper,
                           UserStorage userStorage, FeedbackService feedbackService,
                           ItemRequestStorage itemRequestStorage) {
        this.itemStorage = itemStorage;
        this.itemMapper = itemMapper;
        this.userStorage = userStorage;
        this.feedbackService = feedbackService;
        this.itemRequestStorage = itemRequestStorage;
    }

    static Predicate<Item> isAvailable = item ->
        Boolean.TRUE.equals(item.getAvailable());
    static BiPredicate<Item, String> isMatch = (item, text) -> (item.getName() != null &&
            item.getName().toLowerCase().contains(text)) ||
            (item.getDescription() != null &&
                    item.getDescription().toLowerCase().contains(text));

    public ItemDto create(ItemDto itemDto, Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        Item item = itemMapper.createItem(itemDto, owner);
        itemValidate(item);
        item.setId(itemId++);
        Optional<ItemRequest> request = itemRequestStorage.getItemRequestByItem(item);
        request.ifPresent(item::setRequest);
        itemStorage.create(item);
        return itemMapper.createItemDto(item);
    }

    public ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        if (!item.getOwner().equals(owner)) {
            throw new ObjectNotFoundException("Собственник не тот");
        }
        Item itemToUpdate = itemMapper.createItem(itemDto, owner);
        if (itemToUpdate.getName() != null) {
            item.setName(itemToUpdate.getName());
        }
        if (itemToUpdate.getDescription() != null) {
            item.setDescription(itemToUpdate.getDescription());
        }
        if (itemToUpdate.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        Item updatedItem = itemStorage.update(item);
        return itemMapper.createItemDto(updatedItem);
    }

    public ItemDto getItemById(Integer itemId, Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        return itemMapper.createItemDto(item);
    }

    public List<ItemDto> getItemsByOwner(Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        return itemStorage.getItemsByOwner(owner.getId()).stream()
                .map(itemMapper::createItemDto)
                .collect(toList());
    }

    public List<ItemDto> findAll() {
        return itemStorage.findAll().stream()
                .map(itemMapper::createItemDto)
                .collect(toList());
    }

    public void deleteItem(Integer itemId, Integer ownerId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        if (item.getOwner().getId().equals(owner.getId())) {
            itemStorage.deleteItem(itemId);
        }
    }


    public List<ItemDto> getItemsByQuery(String text, Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        if ((text != null) && (!text.isEmpty()) && (!text.isBlank())) {
            String lowerText = text.toLowerCase();
            return itemStorage.findAll().stream()
                    .filter(item -> isAvailable.test(item) && (isMatch.test(item, lowerText)))
                    .map(itemMapper::createItemDto)
                    .collect(toList());
        }
        return Collections.EMPTY_LIST;
    }

    public FeedbackDto createFeedback(FeedbackDto feedbackDto, Integer itemId, Integer bookerId) {
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        feedbackService.createFeedback(feedbackDto, itemId, booker.getId());
        return feedbackService.getFeedbackById(feedbackDto.getId());
    }

    private void itemValidate(Item item) {
        if (item.getAvailable() == null) {
            throw new ValidationException("Поле статуса не заполнено");
        }
        if (item.getName() == null || item.getName().isEmpty() || item.getName().isBlank()) {
            throw new ValidationException("Некорректное название предмета: " + item.getName());
        }
        if (item.getDescription() == null || item.getDescription().isEmpty() ||
                item.getDescription().isBlank()) {
            throw new ValidationException("Некорректное описание предмета: " + item.getDescription());
        }
    }
}