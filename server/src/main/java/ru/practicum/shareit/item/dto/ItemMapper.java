package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static ItemDto createItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getId());
        }
        return itemDto;
    }

    public static ItemWithCommentsAndBookingsDto createItemWithCommentsAndBookingsDto(Item item) {
        ItemWithCommentsAndBookingsDto itemWithCommentsAndBookingsDto =
                new ItemWithCommentsAndBookingsDto();
        itemWithCommentsAndBookingsDto.setId(item.getId());
        itemWithCommentsAndBookingsDto.setName(item.getName());
        itemWithCommentsAndBookingsDto.setDescription(item.getDescription());
        itemWithCommentsAndBookingsDto.setAvailable(item.getAvailable());
        return itemWithCommentsAndBookingsDto;
    }

    public static ItemForAnswerDto createItemForAnswerDto(Item item, Long requestId) {
        ItemForAnswerDto itemForAnswerDto =
                new ItemForAnswerDto();
        itemForAnswerDto.setId(item.getId());
        itemForAnswerDto.setName(item.getName());
        itemForAnswerDto.setDescription(item.getDescription());
        itemForAnswerDto.setRequestId(requestId);
        itemForAnswerDto.setAvailable(item.getAvailable());
        return itemForAnswerDto;
    }

    public static Item createItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        return item;
    }
}