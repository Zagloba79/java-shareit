package ru.practicum.shareit.request;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
public class ItemRequestService {
    @Autowired
    ItemRequestStorage itemRequestStorage;

    int id = 0;

    public void add(User requester, Item item) {
        ItemRequest request = new ItemRequest(id++, item, requester);
        itemRequestStorage.add(request);
    }

    public void delete(ItemRequest request) {
        itemRequestStorage.delete(request);
    }
}
