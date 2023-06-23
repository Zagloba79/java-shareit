package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Data
@Component
@AllArgsConstructor
public class InMemoryItemRequestStorage implements ItemRequestStorage {
    private HashMap<Integer, ItemRequest> requests = new HashMap<>();
    private Map<ItemRequest, Item> requestToItem = new HashMap<>();

    @Override
    public ItemRequest addRequest(ItemRequest request) {
        requests.put(request.getId(), request);
        return requests.get(request.getId());
    }

    @Override
    public Optional<ItemRequest> getRequestById(int id) {
        return Optional.of(requests.get(id));
    }

    @Override
    public ItemRequest update(ItemRequest request) {
        requests.put(request.getId(), request);
        return requests.get(request.getId());
    }


    @Override
    public Optional<ItemRequest> getItemRequestByItem(Item item) {
        return requests.values().stream()
                .filter(itemRequest -> itemRequest.getDescription().equals(item.getDescription()))
                .findFirst();
    }

    @Override
    public List<ItemRequest> getItemRequestsByRequester(int requesterId) {
        return requests.values().stream()
                .filter(itemRequest -> itemRequest.getRequester().getId() == requesterId)
                .collect(toList());
    }

    @Override
    public void delete(ItemRequest request) {
        requests.remove(request.getId());
    }
}