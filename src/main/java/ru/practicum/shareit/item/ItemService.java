package ru.practicum.shareit.item;

import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Integer ownerId);

    ItemDto getItemById(Integer itemId, Integer ownerId);

    List<ItemDto> getItemsByOwner(Integer ownerId);

    List<ItemDto> findAll();

    void deleteItem(Integer itemId, Integer ownerId);

    List<ItemDto> getItemsByQuery(String text, Integer ownerId);

    FeedbackDto createFeedback(FeedbackDto feedbackDto, Integer itemId, Integer userId);

    ItemDto update(ItemDto itemDto, Integer itemId, Integer ownerId);

}