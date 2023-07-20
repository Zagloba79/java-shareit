package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.handler.EntityHandler;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final EntityHandler handler;

    @Override
    public ItemRequestDto addRequest(ItemRequestDto requestDto, Long requesterId) {
        User requester = handler.getUserFromOpt(requesterId);
        ItemRequest request = ItemRequestMapper.createItemRequest(requestDto);
        requestRepository.save(request);
        ItemRequest requestFromRepository = handler.getRequestFromOpt(request.getId());
        return ItemRequestMapper.createItemRequestDto(requestFromRepository);
    }

    @Override
    public ItemRequestWithAnswersDto getRequestById(Long requestId, Long requesterId) {
        User requester = handler.getUserFromOpt(requesterId);
        ItemRequest request = handler.getRequestFromOpt(requestId);
        List<Item> neededItems = itemRepository.findByRequestIdIn(Collections.singletonList(requestId));
        return ItemRequestMapper
                .createItemRequestWithAnswersDto(request,
                        convertItemsToItemsForAnswer(request.getId(), neededItems));
    }

    @Override
    public List<ItemRequestWithAnswersDto> getRequestsByRequester(Long userId) {
        User requester = handler.getUserFromOpt(userId);
        Sort sortByCreatedDesc = Sort.by(Sort.Direction.DESC, "created");
        List<ItemRequest> itemRequests = requestRepository.findByRequesterId(userId, sortByCreatedDesc);
        List<Long> requestIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        List<Item> allNeededItems = itemRepository.findByRequestIdIn(requestIds);
        List<ItemRequestWithAnswersDto> requestsWithAnswers = new ArrayList<>();
        for (ItemRequest request : itemRequests) {
            List<Item> filtredList = getFiltredList(request.getId(), allNeededItems);
            ItemRequestWithAnswersDto aRequestWithAnswers = ItemRequestMapper
                    .createItemRequestWithAnswersDto(
                            request,
                            convertItemsToItemsForAnswer(request.getId(), filtredList));
            requestsWithAnswers.add(aRequestWithAnswers);
        }
        return requestsWithAnswers;
    }

    @Override
    public ItemRequestDto update(ItemRequestDto itemRequestDto, Long itemId, Long requesterId) {
        Item item = handler.getItemFromOpt(itemId);
        User requester = handler.getUserFromOpt(requesterId);
        Long requestId = itemRequestDto.getId();
        ItemRequest itemRequest = handler.getRequestFromOpt(requestId);
        if (itemRequest.getRequester().equals(requester)) {
            ItemRequest request = ItemRequestMapper.createItemRequest(itemRequestDto);
            requestRepository.save(request);
        }
        ItemRequest itemRequestFromStorage = handler.getRequestFromOpt(requestId);
        return ItemRequestMapper.createItemRequestDto(itemRequestFromStorage);
    }

    @Override
    public void delete(Long requestId, Long requesterId) {
        User requester = handler.getUserFromOpt(requesterId);
        ItemRequest itemRequest = handler.getRequestFromOpt(requestId);
        if (itemRequest.getRequester().equals(requester)) {
            requestRepository.deleteById(requestId);
        }
    }

    private List<Item> getFiltredList(Long requestId, List<Item> allNeededItems) {
        return allNeededItems.stream()
                .filter(item -> item.getRequest().getId().equals(requestId))
                .collect(Collectors.toList());
    }

    private List<ItemForAnswerDto> convertItemsToItemsForAnswer(Long requestId, List<Item> allNeededItems) {
        List<ItemForAnswerDto> items = new ArrayList<>();
        for (Item item : allNeededItems) {
            ItemForAnswerDto itemForAnswerDto = ItemMapper.createItemForAnswerDto(item, requestId);
            items.add(itemForAnswerDto);
        }
        return items;
    }

    @Override
    public List<ItemRequestDto> getRequestsInQuantity(Long userId, Integer from, Integer size) {
        return null;
    }

//    public Collection<ItemRequestDto> getPosts() {
//        // выгружаем посты (один запрос)
//        Map<Long, ItemRequest> postMap = requestRepository.findAll()
//                .stream()
//                .collect(Collectors.toMap(ItemRequest::getId, Function.identity()));
}