package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.feedback.FeedbackService;
import ru.practicum.shareit.feedback.FeedbackStorage;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.dto.FeedbackMapper;
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
    private final FeedbackStorage feedbackStorage;
    private final ItemRequestStorage itemRequestStorage;

    static Predicate<Item> isAvailable = item ->
            Boolean.TRUE.equals(item.getAvailable());
    static BiPredicate<Item, String> isMatch = (item, text) -> (item.getName() != null &&
            item.getName().toLowerCase().contains(text)) ||
            (item.getDescription() != null &&
                    item.getDescription().toLowerCase().contains(text));

    @Override
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        User owner = userStorage.getUser(ownerId);
        Item item = ItemMapper.createItem(itemDto, owner);
        itemValidate(item);
        Optional<ItemRequest> request = itemRequestStorage.getItemRequestByItem(item);
        request.ifPresent(item::setRequest);
        itemStorage.create(item);
        return ItemMapper.createItemDto(item);
    }

    @Override
    public ItemDto update(ItemDto itemDto, Long itemId, Long ownerId) {
        Item item = itemStorage.getItem(itemId);
        User owner = userStorage.getUser(ownerId);
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

    @Override
    public ItemDto getItemById(Long itemId, Long ownerId) {
        User owner = userStorage.getUser(ownerId);
        Item item = itemStorage.getItem(itemId);
        return ItemMapper.createItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        User owner = userStorage.getUser(ownerId);
        return itemStorage.getItemsByOwner(owner.getId()).stream()
                .map(ItemMapper::createItemDto)
                .collect(toList());
    }

    @Override
    public List<ItemDto> findAll() {
        return itemStorage.findAll().stream()
                .map(ItemMapper::createItemDto)
                .collect(toList());
    }

    @Override
    public void deleteItem(Long itemId, Long ownerId) {
        Item item = itemStorage.getItem(itemId);
        User owner = userStorage.getUser(ownerId);
        if (item.getOwner().getId().equals(owner.getId())) {
            itemStorage.deleteItem(itemId);
        }
    }


    @Override
    public List<ItemDto> getItemsByQuery(String text, Long ownerId) {
        User owner = userStorage.getUser(ownerId);
        if ((text != null) && (!text.isBlank())) {
            String lowerText = text.toLowerCase();
            return itemStorage.findAll().stream()
                    .filter(item -> isAvailable.test(item) && (isMatch.test(item, lowerText)))
                    .map(ItemMapper::createItemDto)
                    .collect(toList());
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public FeedbackDto createFeedback(FeedbackDto feedbackDto, Long itemId, Long bookerId) {
        User booker = userStorage.getUser(bookerId);
        feedbackService.createFeedback(feedbackDto, itemId, booker.getId());
        return FeedbackMapper.createFeedbackDto(feedbackStorage.getFeedback(feedbackDto.getId()));
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
}