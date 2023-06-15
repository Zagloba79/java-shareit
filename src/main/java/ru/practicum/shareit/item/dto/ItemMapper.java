package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemMapper {
    public ItemDto createItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(),
                item.isAvailable(), item.getRequest());
    }

    public Item createItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                owner,
                itemDto.getRequest());
    }
}