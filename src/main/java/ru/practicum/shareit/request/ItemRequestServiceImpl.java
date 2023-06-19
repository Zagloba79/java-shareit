package ru.practicum.shareit.request;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class ItemRequestServiceImpl implements ItemRequestService {
    @Autowired
    private InMemoryItemRequestStorage itemRequestStorage;
    private ItemRequestMapper itemRequestMapper;
    private ItemStorage itemStorage;
    private UserStorage userStorage;

    int id = 0;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto, int requesterId) {
        User requester = userStorage.getUser(requesterId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + requesterId + " не существует."));
        ItemRequest request = new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getItem(),
                requester);
        if (request.getId() == null) {
            request.setId(id++);
        }
        itemRequestStorage.addRequest(request);
        ItemRequest itemRequest = itemRequestStorage.getRequestById(request.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + request.getId() + " не существует."));
        return itemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getRequestById(int id) {
        ItemRequest itemRequest = itemRequestStorage.getRequestById(id).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + id + " не существует."));
        return itemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto update(ItemRequestDto itemRequestDto, int itemId, int requesterId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        User requester = userStorage.getUser(requesterId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + requesterId + " не существует."));
        int itemRequestId = itemRequestDto.getId();
        ItemRequest itemRequest = itemRequestStorage.getRequestById(itemRequestId).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + itemRequestId + " не существует."));
        if (itemRequest.getRequester().equals(requester) && itemRequestDto.getItem().equals(item)) {
            ItemRequest request = itemRequestMapper.createItemRequest(itemRequestDto);
            itemRequestStorage.update(request);
        }
        ItemRequest itemRequestFromStorage = itemRequestStorage.getRequestById(itemRequestId).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + itemRequestId + " не существует."));
        return itemRequestMapper.createItemRequestDto(itemRequestFromStorage);
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByItem(Item item) {
        return itemRequestStorage.getItemRequestsByItem(item).stream()
                .map(itemRequestMapper::createItemRequestDto)
                .collect(toList());
    }

    @Override
    public void delete(int itemRequestId, int requesterId) {
        User requester = userStorage.getUser(requesterId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + requesterId + " не существует."));
        ItemRequest itemRequest = itemRequestStorage.getRequestById(itemRequestId).orElseThrow(() ->
                new ObjectNotFoundException("Запроса с " + itemRequestId + " не существует."));
        if (itemRequest.getRequester().equals(requester)) {
            itemRequestStorage.delete(itemRequest);
        }
    }

    @Override
    public List<ItemRequestDto> getItemsByRequester(int requesterId) {
        return itemRequestStorage.getItemRequestsByRequester(requesterId).stream()
                .map(itemRequestMapper::createItemRequestDto)
                .collect(toList());
    }
}