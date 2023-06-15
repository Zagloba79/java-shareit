package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.HashMap;

@Data
public class ItemRequestStorage {
    private HashMap<Item, ItemRequest> requests = new HashMap<>();

    public void add(ItemRequest request) {
        requests.put(request.getItem(), request);
    }

    public void delete(ItemRequest request) {
        requests.remove(request.getItem());
    }
}
