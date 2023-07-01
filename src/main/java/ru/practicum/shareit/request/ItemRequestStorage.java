package ru.practicum.shareit.request;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestStorage {
    ItemRequest addRequest(ItemRequest request);

    ItemRequest getRequest(Integer id);

    ItemRequest update(ItemRequest request);

    Optional<ItemRequest> getItemRequestByItem(Item item);

    List<ItemRequest> getItemRequestsByRequester(int requesterId);

    void delete(ItemRequest request);
}