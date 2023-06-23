package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
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
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemStorage itemStorage;
    private ItemMapper itemMapper;
    private UserStorage userStorage;
    private FeedbackService feedbackService;
    private ItemRequestStorage itemRequestStorage;
    private static Integer id = 1;

    public ItemDto create(ItemDto itemDto, Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        Item item = itemMapper.createItem(itemDto, owner);
        item.setId(id++);
        Optional<ItemRequest> request = itemRequestStorage.getItemRequestByItem(item);
        request.ifPresent(item::setRequest);
        return itemMapper.createItemDto(itemStorage.create(item));
    }

    public ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        if (Objects.equals(item.getOwner().getId(), owner.getId())) {
            item.setName(itemDto.getName());
            item.setDescription(itemDto.getDescription());
            item.setAvailable(itemDto.isAvailable());
            itemStorage.update(item);
        }
        Item updatedItem = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Что-то пошло не так"));
        return itemMapper.createItemDto(updatedItem);
    }

    public ItemDto getItemById(Integer itemId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        return itemMapper.createItemDto(item);
    }

    public List<ItemDto> getItemsByOwner(Integer ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        return itemStorage.getItemsByOwner(owner.getId()).stream()
                .map(itemMapper::createItemDtoForOwner)
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
        if (Objects.equals(item.getOwner().getId(), owner.getId())) {
            itemStorage.deleteItem(itemId);
        }
    }


    public List<ItemDto> getItemsByQuery(String text) {
        if ((text != null) && (!text.isEmpty()) && (!text.isBlank())) {
            String lowerText = text.toLowerCase();
            return itemStorage.findAll().stream()
                    .filter(item -> (item.isAvailable() &&
                            (item.getName() != null &&
                            item.getName().toLowerCase().contains(lowerText)) ||
                            (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerText))))
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
}