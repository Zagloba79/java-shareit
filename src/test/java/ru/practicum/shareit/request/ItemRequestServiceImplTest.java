package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.handler.EntityHandler;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {
    private final EasyRandom generator = new EasyRandom();
    @InjectMocks
    private ItemRequestServiceImpl service;
    @Mock
    ItemRequestRepository requestRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    EntityHandler handler;
    Long requesterId = 1L;
    Long requestId = 3L;
    User requester;
    ItemRequestDto requestDto;
    ItemRequest request;
    Item item;
    @BeforeEach
    public void setUp() {
        requester = generator.nextObject(User.class);
        requestDto = generator.nextObject(ItemRequestDto.class);
        requestDto.setDescription("description");
        request = new ItemRequest(3L, "description",
                requester, LocalDate.of(2023, 7, 3));
        item = generator.nextObject(Item.class);
    }

    @Test
    public void createRequestTest() {
        when(handler.getUserFromOpt(Mockito.anyLong()))
                .thenReturn(requester);
        when(requestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(request);
        ItemRequestDto itemRequestDto = service.create(requestDto, requester.getId());
        assertEquals("description", itemRequestDto.getDescription());
    }

    @Test
    public void getRequestByIdTest() {
        when(handler.getUserFromOpt(Mockito.anyLong()))
                .thenReturn(requester);
        when(handler.getRequestFromOpt(Mockito.anyLong()))
                .thenReturn(request);
        when(itemRepository.findAllByRequestIds(Collections.singletonList(requestId)))
                .thenReturn(List.of(item));
        RequestWithItemsDto requestWithItemsDto = service.getRequestById(
                requestId, requesterId);
        assertEquals("description", requestWithItemsDto.getDescription());
        assertEquals(LocalDate.of(2023, 7, 3), requestWithItemsDto.getCreated());
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
        when(handler.getUserFromOpt(Mockito.anyLong()))
                .thenReturn(requester);
        List<RequestWithItemsDto> requestsByRequester = service.getRequestsByRequester(requesterId);
        assertEquals(3, requestsByRequester.size());
    }

    @Test
    public void getRequestsPageableTest() {
        int from = 1;
        int size = 2;
        User owner = generator.nextObject(User.class);
        List<ItemRequest> requests = new ArrayList<>();
        requests.add(new ItemRequest(21L, "description1",
                owner, LocalDate.of(2023, 7, 1)));
        requests.add(new ItemRequest(22L, "description2",
                owner, LocalDate.of(2023, 7, 2)));
        requests.add(new ItemRequest(23L, "description3",
                owner, LocalDate.of(2023, 7, 3)));
        Sort sort = Sort.by(Sort.Direction.ASC, "created");
        Pageable pageable = PageRequest.of(from, size, sort);
        when(requestRepository.findAllByRequesterIdNot(requesterId, pageable))
                .thenReturn(requests);
        when(handler.getUserFromOpt(Mockito.anyLong()))
                .thenReturn(requester);
        List<RequestWithItemsDto> requestsByRequesterNot = service.getRequestsPageable(
                requesterId, from, size);
        //assertEquals(22, requestsByRequesterNot.get(0).getId());
    }
}
