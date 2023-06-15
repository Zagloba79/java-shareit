package ru.practicum.shareit.item.model;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item create(String name, String description, boolean available, User owner, String request);
    void delete(Item item);
    Optional<Item> getItem(Integer id);
    List<Item> findAll();
    Item update(Item item);
}
