package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class Searcher {
    @Autowired
    InMemoryItemStorage itemStorage;
    public Item searchItemById(Integer id) {
        return itemStorage.getItem(id).orElseThrow(() ->
                new ObjectNotFoundException("item with id = " + id + " not found"));
    }

    public List<Item> searchItemsByName(String name) {
        List<Item> itemsByName = new ArrayList<>();
        for (Item item : itemStorage.findAll()) {
            if (item.getName().equals(name)) {
                itemsByName.add(item);
            }
        }
        return itemsByName;
    }
}
