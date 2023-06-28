package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final FeedbackService feedbackService;
    private final ItemRequestStorage itemRequestStorage;

    static Predicate<Item> isAvailable = item ->
        Boolean.TRUE.equals(item.getAvailable());
    static BiPredicate<Item, String> isMatch = (item, text) -> (item.getName() != null &&
            item.getName().toLowerCase().contains(text)) ||
            (item.getDescription() != null &&
                    item.getDescription().toLowerCase().contains(text));

    public ItemDto create(ItemDto itemDto, Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        Item item = ItemMapper.createItem(itemDto, owner);
        itemValidate(item);
        Optional<ItemRequest> request = itemRequestStorage.getItemRequestByItem(item);
        request.ifPresent(item::setRequest);
        itemStorage.create(item);
        return ItemMapper.createItemDto(item);
    }

    public ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
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
        Item updatedItem = itemStorage.update(item);
        return ItemMapper.createItemDto(updatedItem);
    }

    public ItemDto getItemById(Integer itemId, Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        return ItemMapper.createItemDto(item);
    }

    public List<ItemDto> getItemsByOwner(Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        return itemStorage.getItemsByOwner(owner.getId()).stream()
                .map(ItemMapper::createItemDto)
                .collect(toList());
    }

    public List<ItemDto> findAll() {
        return itemStorage.findAll().stream()
                .map(ItemMapper::createItemDto)
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
        if ((text != null) && (!text.isBlank())) {
            String lowerText = text.toLowerCase();
            return itemStorage.findAll().stream()
                    .filter(item -> isAvailable.test(item) && (isMatch.test(item, lowerText)))
                    .map(ItemMapper::createItemDto)
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
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Некорректное название предмета: " + item.getName());
        }
        if (item.getDescription() == null  || item.getDescription().isBlank()) {
            throw new ValidationException("Некорректное описание предмета: " + item.getDescription());
        }
    }
}