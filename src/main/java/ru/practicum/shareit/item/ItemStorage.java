package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    void deleteItem(Long itemId);

    Item getItem(Long id);

    List<Item> findAll();

    Item update(Item item);

    List<Item> getItemsByOwner(Long ownerId);
}