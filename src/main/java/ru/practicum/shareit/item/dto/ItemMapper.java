package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public final class ItemMapper {
    private ItemMapper() {
    }

    public static ItemDto createItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
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