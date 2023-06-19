package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component("itemStorage")
@Slf4j
@AllArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private ItemStorage itemStorage;
    Integer itemId = 0;
    Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Optional<Item> getItemById(Integer id) {
        return Optional.of(items.get(id));
    }

    @Override
    public void deleteItem(int itemId) {
        items.remove(itemId);
    }

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    public List<Item> getItemsByOwner(int ownerId) {
        return itemStorage.findAll().stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(toList());
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }
}