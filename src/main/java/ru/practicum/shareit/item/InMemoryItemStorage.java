package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component("itemStorage")
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();
    private Integer itemId = 1;

    @Override
    public Item create(Item item) {
        item.setId(itemId++);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Optional<Item> getItemById(Integer id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public void deleteItem(int itemId) {
        items.remove(itemId);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getItemsByOwner(int ownerId) {
        return findAll().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .collect(toList());
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }
}