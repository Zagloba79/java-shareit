package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component("itemStorage")
@Slf4j
@AllArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private ItemMapper itemMapper;
    private ItemStorage itemStorage;
    Integer itemId = 0;
    Map<Integer, Item> items = new HashMap<>();

    @Override
    public ItemDto create(Item item) {
        items.put(item.getId(), item);
        return itemMapper.createItemDto(items.get(item.getId()));
    }

    @Override
    public void deleteItem(int itemId, int ownerId) {
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

    public List<Item> getItemsByOwner(int ownerId) {
        return itemStorage.findAll().stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(toList());
    }

    @Override
    public Item update(Item item) {
        return null;
    }
}
