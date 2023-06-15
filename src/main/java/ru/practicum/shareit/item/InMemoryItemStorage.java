package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component("itemStorage")
@Slf4j
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

    @Override
    public void delete(int itemId, int ownerId) {
        if (!items.containsKey(itemId)) {
            log.info("Предмет с идентификатором {} не найден.", itemId);
            throw new ObjectNotFoundException("Нет такого пользователя");
        } else {
            Item item = items.get(itemId);
        }

    }

    public Item add(Item item) {
        items.put(item.getId(), item);
        return item;
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
