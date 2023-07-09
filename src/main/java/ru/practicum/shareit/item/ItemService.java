package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsDto;
import ru.practicum.shareit.item.dto.ItemWithDatesAndCommentsDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemWithCommentsDto getItemById(Long itemId, Long ownerId);

    List<ItemWithDatesAndCommentsDto> getItemsByOwner(Long ownerId);

    List<ItemDto> findAll();

    void deleteItem(Long itemId, Long ownerId);

    List<ItemDto> getItemsByQuery(String text, Long ownerId);

    CommentDto createComment(CommentDto feedbackDto, Long itemId, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);
}