package ru.practicum.shareit.item;

import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long ownerId);

    ItemDto getItemById(Long itemId, Long ownerId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    List<ItemDto> findAll();

    void deleteItem(Long itemId, Long ownerId);

    List<ItemDto> getItemsByQuery(String text, Long ownerId);

    FeedbackDto createFeedback(FeedbackDto feedbackDto, Long itemId, Long userId);

    ItemDto update(ItemDto itemDto, Long itemId, Long ownerId);
}