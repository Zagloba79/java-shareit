package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemRequestServiceShouldTest {
    private final ItemRequestService service;
    private final ItemRequestRepository repository;
    private final UserService userService;
    private User user;
    private final LocalDateTime created = LocalDateTime.of(
            2023, 1,2,3,4,5);

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto("name", "q@ma.ru");
        user = UserMapper.createUser(userService.create(userDto));
    }

    @AfterEach
    void clearDb() {
        repository.deleteAll();
        List<UserDto> users = userService.findAll();
        for (UserDto user : users) {
            userService.delete(user.getId());
        }
    }

    @Test
    void shouldReturnRequestWhenGetRequestById() {
        ItemRequestDto requestDto = new ItemRequestDto("description", created);
        ItemRequestDto requestFromDb = service.create(requestDto, user.getId());
        assertEquals(requestFromDb.getDescription(), requestDto.getDescription());
    }

    @Test
    void exceptionWhenGetRequestByWrongId() {
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> service.getRequestById(1L, user.getId()));
        assertEquals("Запроса с " + 1L + " не существует.",
                exception.getMessage());
    }

    @Test
    void exceptionWhenGetRequestByWrongRequesterId() {
        ItemRequestDto dto = new ItemRequestDto("description", LocalDateTime.now());
        ItemRequestDto requestDto = service.create(dto, user.getId());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> service.getRequestById(requestDto.getId(), 5L));
        assertEquals("Пользователя с " + 5L + " не существует.",
                exception.getMessage());
    }

//    @Override
//    public RequestWithItemsDto getRequestById(Long requestId, Long requesterId) {
//        User requester = handler.getUserFromOpt(requesterId);
//        ItemRequest request = handler.getRequestFromOpt(requestId);
//        List<Item> neededItems = itemRepository.findAllByRequestIdIn(
//                Collections.singletonList(requestId), Sort.by( "id").ascending());
//        List<ItemForAnswerDto> itemsForAnswer = getMappedList(requestId, neededItems);
//        return ItemRequestMapper
//                .createItemRequestWithAnswersDto(request, itemsForAnswer);
//    }
//
//    @Override //TODO
//    public List<RequestWithItemsDto> getRequestsByRequester(Long userId) {
//        User requester = handler.getUserFromOpt(userId);
//        List<ItemRequest> requests = requestRepository.findByRequesterId(requester.getId(),
//                Sort.by( "created").descending());
//        if (requests.size() == 0) {
//            return Collections.EMPTY_LIST;
//        }
//        List<Item> allNeededItems = createAllNeededItemsList(requests);
//        return getRequestsWithItems(requests, allNeededItems);
//    }
//
//    @Override//TODO
//    public List<RequestWithItemsDto> getRequestsPageable(Long userId, Integer from, Integer size) {
//        User user = handler.getUserFromOpt(userId);
//        Pageable pageable = PageRequest.of(from, size, Sort.by( "created").ascending());
//        List<ItemRequest> requests = requestRepository.findAllByRequesterIdNot(userId, pageable);
//        if (requests.size() > 0) {
//            List<Item> allNeededItems = createAllNeededItemsList(requests);
//            return getRequestsWithItems(requests, allNeededItems);
//        }
//        return Collections.emptyList();
//    }
}