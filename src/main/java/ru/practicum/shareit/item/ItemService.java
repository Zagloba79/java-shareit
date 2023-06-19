package ru.practicum.shareit.item;

import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, int ownerId);

    ItemDto getItemById(int itemId);

    List<ItemDto> getItemsByOwner(int ownerId);

    List<ItemDto> findAll();

    void deleteItem(int itemId, int ownerId);

    List<ItemDto> getItemsByQuery(String text);

    FeedbackDto createFeedback(FeedbackDto feedbackDto, int itemId, int userId);

    ItemDto update(ItemDto itemDto, int itemId, int ownerId);
}