package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class InMemoryItemRequestStorage implements ItemRequestStorage {
    private final HashMap<Long, ItemRequest> requests = new HashMap<>();
    private Long itemRequestId = 1L;

    @Override
    public ItemRequest addRequest(ItemRequest request) {
        request.setId(itemRequestId++);
        requests.put(request.getId(), request);
        return requests.get(request.getId());
    }

    private Optional<ItemRequest> getRequestOpt(Long id) {
        return Optional.of(requests.get(id));
    }

    @Override
    public ItemRequest getRequest(Long id) {
        return getRequestOpt(id).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + id + " не существует."));
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
    public List<ItemRequest> getItemRequestsByRequester(Long requesterId) {
        return requests.values().stream()
                .filter(itemRequest -> Objects.equals(itemRequest.getRequester().getId(), requesterId))
                .collect(toList());
    }

    @Override
    public void delete(ItemRequest request) {
        requests.remove(request.getId());
    }
}