package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemMapper {
    private UserStorage userStorage;
    public ItemDto createItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(),
                item.isAvailable(), item.getRequest());
    }

    public Item createItem(ItemDto itemDto, int ownerId, int id) {
        User owner = userStorage.getUser(ownerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + ownerId + " не существует."));
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.isAvailable(),
                owner,
                itemDto.getRequest());
    }
}