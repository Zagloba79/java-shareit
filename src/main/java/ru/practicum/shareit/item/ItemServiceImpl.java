package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.feedback.Feedback;
import ru.practicum.shareit.feedback.FeedbackService;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
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

    public ItemDto create(ItemDto itemDto, int ownerId) {
        return itemStorage.create(itemMapper.createItem(itemDto, ownerId, ownerId));
    }

    public ItemDto update(ItemDto itemDto, int itemId, int ownerId) {
        Item item = itemMapper.createItem(itemDto, itemId, ownerId);
        if (itemStorage.findAll().contains(item)) {
            itemStorage.update(item);
        }
        return itemMapper.createItemDto(itemStorage.getItem(item.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + item.getId() + " не существует.")));
    }


    public ItemDto getItemById(int itemId, int ownerId) {
        return itemMapper.createItemDto(itemStorage.getItem(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует.")));
    }

    public List<ItemDto> getItemsByOwner(int ownerId) {
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

    public void deleteItem(int itemId, int ownerId) {
        itemStorage.deleteItem(itemId, ownerId);
    }


    public List<ItemDto> getItemsByQuery(String text) {
        List<Item> itemsByQuery = new ArrayList<>();
        if ((text != null) && (!text.isEmpty()) && (!text.isBlank())) {
            String lowerText = text.toLowerCase();
            itemsByQuery = itemStorage.findAll().stream()
                    .filter(item -> item.getName().contains(lowerText) ||
                                    item.getDescription().contains(lowerText))
                    .collect(toList());
            for (Item item : itemStorage.findAll()) {
                List<Feedback> feedbacks = feedbackService.getFeedbacksByItem(item);
                for (Feedback feedback : feedbacks) {
                    if (feedback.getComment().toLowerCase().contains(lowerText)) {
                        itemsByQuery.add(item);
                        break;
                    }
                }
            }
        }
        return itemsByQuery.stream()
                .map(itemMapper::createItemDto)
                .collect(toList());
    }

    public FeedbackDto createFeedback(FeedbackDto feedbackDto, int itemId, int userId) {

    }
}
