package ru.practicum.shareit.item;

import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public class ItemService {
    ItemStorage itemStorage;

    public ItemDto create(ItemDto itemDto, int ownerId) {
    }

    public ItemDto update(ItemDto itemDto, int itemIdint, int ownerId) {
        
    }

    public ItemDto getItemById(int itemId, int ownerId) {
    }

    public List<ItemDto> getItemsByOwner(int ownerId) {
    }
    public List<Item> findAll() {
        return itemStorage.findAll();
    }

    public void delete(int itemId, int ownerId) {
        itemStorage.delete(itemId, ownerId);
    }


    public List<ItemDto> getItemsByQuery(String text) {
    }

    public FeedbackDto createFeedback(FeedbackDto feedbackDto, Long itemId, Long userId) {
    }
}
