package ru.practicum.shareit.item.model;

import java.util.List;
import java.util.Optional;

public class ItemService {
    ItemStorage itemStorage;

//    public Item create(Item item) {
//        itemStorage.create(item);
//        return item;
//    }

    public List<Item> findAll() {
        return itemStorage.findAll();
    }

    public Optional<Item> getItem(Integer itemId) {
        return itemStorage.getItem(itemId);
    }

    public void delete(Item item) {
        itemStorage.delete(item);
    }

    public Item update(Item item) {
        return itemStorage.update(item);
    }
}
