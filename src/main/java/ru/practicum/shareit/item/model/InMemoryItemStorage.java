package ru.practicum.shareit.item.model;

import ru.practicum.shareit.user.User;

import java.util.*;

public class InMemoryItemStorage implements ItemStorage {
    Integer itemId = 0;
    Map<Integer, Item> items = new HashMap<>();

    public Item create(String name, String description, boolean available, User owner, String request) {
        Item item = new Item(name, description, available, owner, request);
        int id = itemId++;
        item.setId(id);
        add(item);
        return item;
    }

    public Item add(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public void delete(Item item) {
        items.remove(item.getId());
    }

    public Optional<Item> getItem(Integer id) {
        return Optional.of(items.get(id));
    }

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item update(Item item) {
        return null;
    }
}
