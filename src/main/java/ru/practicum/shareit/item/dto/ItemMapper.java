package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

@Component
@AllArgsConstructor
public class ItemMapper {
    private final UserStorage userStorage;

    public ItemDto createItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequests());
    }

    public ItemDto createItemDtoForOwner(Item item) {
        return new ItemDto(item.getName(), item.getDescription());
    }

    public Item createItem(ItemDto itemDto, User owner) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                owner,
                itemDto.getRequests());
    }
}