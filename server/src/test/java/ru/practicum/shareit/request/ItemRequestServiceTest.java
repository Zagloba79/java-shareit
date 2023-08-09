package ru.practicum.shareit.request;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceTest {
    private final EasyRandom generator = new EasyRandom();
    @InjectMocks
    private ItemRequestServiceImpl service;
    @Mock
    ItemRequestRepository requestRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    EntityHandler handler;
    final Long requesterId = 1L;
    final Long requestId = 3L;
    final User requester = generator.nextObject(User.class);
    ItemRequestDto requestDto;
    final ItemRequest request = new ItemRequest(3L, "description",
            requester, LocalDateTime.of(2023, 7, 3, 0, 0, 0));
    final Item item = generator.nextObject(Item.class);
    final List<ItemRequest> requests = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        requestDto = generator.nextObject(ItemRequestDto.class);
        requestDto.setDescription("description");
    }

    @Test
    public void createRequestTest() {
        when(handler.getUserFromOpt(anyLong()))
                .thenReturn(requester);
        when(requestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(request);
        ItemRequestDto itemRequestDto = service.create(requestDto, requester.getId());
        assertEquals("description", itemRequestDto.getDescription());
    }

    @Test
    public void getRequestByIdTest() {
        when(handler.getUserFromOpt(anyLong()))
                .thenReturn(requester);
        when(handler.getRequestFromOpt(anyLong()))
                .thenReturn(request);
        when(itemRepository.findAllByRequestIdIn(Collections.singletonList(requestId),
                Sort.by("id").ascending()))
                .thenReturn(List.of(item));
        RequestWithItemsDto requestWithItemsDto = service.getRequestById(
                requestId, requesterId);
        assertEquals("description", requestWithItemsDto.getDescription());
        assertEquals(LocalDateTime.of(2023, 7, 3, 0, 0, 0),
                requestWithItemsDto.getCreated());
    }

    @Test
    public void getRequestsByRequesterTest() {
        List<ItemRequest> requests = new ArrayList<>();
        requests.add(generator.nextObject(ItemRequest.class));
        requests.add(generator.nextObject(ItemRequest.class));
        requests.add(generator.nextObject(ItemRequest.class));
        when(requestRepository.findByRequesterId(requester.getId(),
                Sort.by("created").descending()))
                .thenReturn(requests);
        when(handler.getUserFromOpt(anyLong()))
                .thenReturn(requester);
        List<RequestWithItemsDto> requestsByRequester = service.getRequestsByRequester(requesterId);
        assertEquals(3, requestsByRequester.size());
    }

    @Test
    public void getRequestsPageableTest() {
        User owner = generator.nextObject(User.class);
        owner.setId(30L);
        requests.add(generator.nextObject(ItemRequest.class));
        requests.add(generator.nextObject(ItemRequest.class));
        when(requestRepository.findAllByRequesterIdNot(requester.getId(),
                PageRequest.of(1, 10, Sort.by("created").ascending())))
                .thenReturn(requests);
        when(handler.getUserFromOpt(anyLong()))
                .thenReturn(requester);
        List<RequestWithItemsDto> requestsByRequesterNot = service.getRequestsPageable(
                requester.getId(), 1, 10);
        assertEquals(2, requestsByRequesterNot.size());
    }
}