package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemStorage itemStorage;
    private ItemMapper itemMapper;
    private UserStorage userStorage;
    private FeedbackService feedbackService;
    private ItemRequestStorage itemRequestStorage;
    private int id = 0;

    public ItemDto create(ItemDto itemDto, int ownerId) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        Item item = itemMapper.createItem(itemDto, owner.getId());
        if (item.getId() == null) {
            item.setId(id++);
        }
        List<String> requests = itemRequestStorage.getItemRequestsByItem(item)
                .stream()
                .map(ItemRequest::toString)
                .collect(toList());
        item.setRequests(requests);
        return itemMapper.createItemDto(itemStorage.create(item));
    }

    public ItemDto update(ItemDto itemDto, int itemId, int ownerId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        if (item.getOwner().getId() == owner.getId()) {
            item.setName(itemDto.getName());
            item.setDescription(itemDto.getDescription());
            item.setAvailable(itemDto.isAvailable());
            itemStorage.update(item);
        }
        Item updatedItem = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Что-то пошло не так"));
        return itemMapper.createItemDto(updatedItem);
    }

    public ItemDto getItemById(int itemId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        return itemMapper.createItemDto(item);
    }

    public List<ItemDto> getItemsByOwner(int ownerId) {
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

    public void deleteItem(int itemId, int ownerId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        if (item.getOwner().getId() == owner.getId()) {
            itemStorage.deleteItem(itemId);
        }
    }


    public List<ItemDto> getItemsByQuery(String text) {
        List<ItemDto> itemsByQuery = new ArrayList<>();
        if ((text != null) && (!text.isEmpty()) && (!text.isBlank())) {
            String lowerText = text.toLowerCase();
            itemsByQuery = itemStorage.findAll().stream()
                    .filter(item -> (item.getName().toLowerCase().contains(lowerText) ||
                            item.getDescription().toLowerCase().contains(lowerText))
                            && item.isAvailable())
                    .map(itemMapper::createItemDto)
                    .collect(toList());
        }
        return itemsByQuery;
    }

    public FeedbackDto createFeedback(FeedbackDto feedbackDto, int itemId, int bookerId) {
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        feedbackService.createFeedback(feedbackDto, itemId, booker.getId());
        return feedbackService.getFeedbackById(feedbackDto.getId());
    }
}