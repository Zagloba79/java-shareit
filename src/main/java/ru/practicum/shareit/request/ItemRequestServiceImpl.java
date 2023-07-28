package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final EntityHandler handler;

    @Override
    public ItemRequestDto create(ItemRequestDto requestDto, Long requesterId) {
        LocalDateTime presentTime = LocalDateTime.now();
        User requester = handler.getUserFromOpt(requesterId);
        if (requestDto.getDescription() == null || requestDto.getDescription().isBlank()) {
            throw new ValidationException("Запрос без описания");
        }
        ItemRequest request = ItemRequestMapper.createNewItemRequest(requestDto, requester);
        request.setCreated(presentTime);
        return ItemRequestMapper.createItemRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestWithItemsDto getRequestById(Long requestId, Long requesterId) {
        User requester = handler.getUserFromOpt(requesterId);
        ItemRequest request = handler.getRequestFromOpt(requestId);
        List<Item> neededItems = itemRepository.findAllByRequestIdIn(
                Collections.singletonList(requestId), Sort.by("id").ascending());
        List<ItemForAnswerDto> itemsForAnswer = getMappedList(requestId, neededItems);
        return ItemRequestMapper
                .createItemRequestWithAnswersDto(request, itemsForAnswer);
    }

    @Override
    public List<RequestWithItemsDto> getRequestsByRequester(Long userId) {
        User requester = handler.getUserFromOpt(userId);
        List<ItemRequest> requests = requestRepository.findByRequesterId(requester.getId(),
                Sort.by("created").descending());
        if (requests.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> allNeededItems = createAllNeededItemsList(requests);
        return getRequestsWithItems(requests, allNeededItems);
    }

    @Override
    public List<RequestWithItemsDto> getRequestsPageable(Long userId, Integer from, Integer size) {
        User user = handler.getUserFromOpt(userId);
        Pageable pageable = PageRequest.of(from, size, Sort.by("created").ascending());
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdNot(userId, pageable);
        if (requests.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> allNeededItems = createAllNeededItemsList(requests);
        return getRequestsWithItems(requests, allNeededItems);
    }

    private List<Item> createAllNeededItemsList(List<ItemRequest> requests) {
        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        return itemRepository.findAllByRequestIdIn(requestIds, Sort.by("id").ascending());
    }

    private List<RequestWithItemsDto> getRequestsWithItems(List<ItemRequest> requests,
                                                           List<Item> allNeededItems) {
        List<RequestWithItemsDto> requestsWithAnswers = new ArrayList<>();
        for (ItemRequest request : requests) {
            List<Item> filteredList = getFilteredListForARequest(request.getId(), allNeededItems);
            List<ItemForAnswerDto> itemsForAnswer = getMappedList(request.getId(), filteredList);
            RequestWithItemsDto aRequestWithAnswers = ItemRequestMapper
                    .createItemRequestWithAnswersDto(request, itemsForAnswer);
            requestsWithAnswers.add(aRequestWithAnswers);
        }
        return requestsWithAnswers;
    }

    private List<Item> getFilteredListForARequest(Long requestId, List<Item> allNeededItems) {
        return allNeededItems.stream()
                .filter(item -> item.getRequest().getId().equals(requestId))
                .collect(Collectors.toList());
    }

    private List<ItemForAnswerDto> getMappedList(Long requestId, List<Item> filteredList) {
        List<ItemForAnswerDto> items = new ArrayList<>();
        for (Item item : filteredList) {
            ItemForAnswerDto itemForAnswerDto = ItemMapper.createItemForAnswerDto(item, requestId);
            items.add(itemForAnswerDto);
        }
        return items;
    }
}