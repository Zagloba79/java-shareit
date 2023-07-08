package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.handler.OptionalHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final OptionalHandler optionalHandler;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto requestDto, Long requesterId) {
        User requester = optionalHandler.getUserFromOpt(requesterId);
        ItemRequest request = ItemRequestMapper.createItemRequest(requestDto);
        requestRepository.save(request);
        ItemRequest requestFromRepository = optionalHandler.getRequestFromOpt(request.getId());
        return ItemRequestMapper.createItemRequestDto(requestFromRepository);
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId) {
        ItemRequest itemRequest = optionalHandler.getRequestFromOpt(requestId);
        return ItemRequestMapper.createItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto update(ItemRequestDto itemRequestDto, Long itemId, Long requesterId) {
        Item item = optionalHandler.getItemFromOpt(itemId);
        User requester = optionalHandler.getUserFromOpt(requesterId);
        Long requestId = itemRequestDto.getId();
        ItemRequest itemRequest = optionalHandler.getRequestFromOpt(requestId);
        if (itemRequest.getRequester().equals(requester)) {
            ItemRequest request = ItemRequestMapper.createItemRequest(itemRequestDto);
            requestRepository.save(request);
        }
        ItemRequest itemRequestFromStorage = optionalHandler.getRequestFromOpt(requestId);
        return ItemRequestMapper.createItemRequestDto(itemRequestFromStorage);
    }

    @Override
    public void delete(Long requestId, Long requesterId) {
        User requester = optionalHandler.getUserFromOpt(requesterId);
        ItemRequest itemRequest = optionalHandler.getRequestFromOpt(requestId);
        if (itemRequest.getRequester().equals(requester)) {
            requestRepository.deleteById(requestId);
        }
    }

    @Override
    public List<ItemRequestDto> getRequestsDtoByRequester(Long requesterId) {
        User requester = optionalHandler.getUserFromOpt(requesterId);
        return requestRepository.findAll().stream()
                .filter(itemRequest -> itemRequest.getRequester().getId().equals(requesterId))
                .map(ItemRequestMapper::createItemRequestDto)
                .collect(toList());
    }
}