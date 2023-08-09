package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsAndBookingsDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long ownerId);

    ItemWithCommentsAndBookingsDto getItemById(Long itemId, Long ownerId);

    List<ItemWithCommentsAndBookingsDto> getItemsByOwnerPageable(Long ownerId,
                                                                 Integer from,
                                                                 Integer size);

    void deleteItem(Long itemId, Long ownerId);

    List<ItemDto> getItemsByQueryPageable(Integer from, Integer size, String text, Long ownerId);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);
}