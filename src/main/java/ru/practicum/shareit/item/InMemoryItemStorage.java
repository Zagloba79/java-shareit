package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component("itemStorage")
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private Long itemId = 1L;

    @Override
    public Item create(Item item) {
        item.setId(itemId++);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    private Optional<Item> getItemOpt(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item getItem(Long id) {
        return getItemOpt(id).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
    }

    @Override
    public void deleteItem(Long id) {
        items.remove(id);
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public List<Item> getItemsByOwner(Long ownerId) {
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