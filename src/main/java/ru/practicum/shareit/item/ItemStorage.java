package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item create(String name, String description, boolean available, User owner, String request);
    void delete(int itemId, int ownerId);
    Optional<Item> getItem(Integer id);
    List<Item> findAll();
    Item update(Item item);


}
