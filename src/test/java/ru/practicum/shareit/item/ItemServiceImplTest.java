//package ru.practicum.shareit.item;
//
//import org.jeasy.random.EasyRandom;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import ru.practicum.shareit.exception.ObjectNotFoundException;
//import ru.practicum.shareit.exception.ValidationException;
//import ru.practicum.shareit.handler.EntityHandler;
//import ru.practicum.shareit.item.dto.*;
//import ru.practicum.shareit.item.model.Comment;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.request.ItemRequestRepository;
//import ru.practicum.shareit.request.model.ItemRequest;
//import ru.practicum.shareit.user.model.User;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import static java.util.stream.Collectors.toList;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//
//public class ItemServiceImplTest {
//    private final EasyRandom generator = new EasyRandom();
//    @InjectMocks
//    private ItemServiceImpl service;
//    @MockBean
//    ItemRequestRepository requestRepository;
//    @MockBean
//    EntityHandler handler;
//    User requester;
//    ItemDto itemDto;
//
//    @BeforeEach
//    public void setUp() {
//        requester = generator.nextObject(User.class);
//        itemDto = generator.nextObject(ItemDto.class);
//    }
//
//    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
//        handler.itemValidate(itemDto);
//        User owner = handler.getUserFromOpt(ownerId);
//        Item item = ItemMapper.createItem(itemDto, owner);
//        if (itemDto.getRequestId() != null) {
//            ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow(() ->
//                    new ObjectNotFoundException("Такого запроса не существует"));
//            item.setRequest(request);
//        }
//        itemRepository.save(item);
//        return ItemMapper.createItemDto(item);
//    }
//
//    @Test
//    public void createItemTest() {
//        doNothing().when(handler).itemValidate(itemDto);
//        when(handler.getUserFromOpt(Mockito.anyLong()))
//                .thenReturn(requester);
//
//    }
//
//    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
//        Item item = handler.getItemFromOpt(itemId);
//        User owner = handler.getUserFromOpt(ownerId);
//        if (!item.getOwner().equals(owner)) {
//            throw new ObjectNotFoundException("Собственник не тот");
//        }
//        if (itemDto.getName() != null) {
//            item.setName(itemDto.getName());
//        }
//        if (itemDto.getDescription() != null) {
//            item.setDescription(itemDto.getDescription());
//        }
//        if (itemDto.getAvailable() != null) {
//            item.setAvailable(itemDto.getAvailable());
//        }
//        itemRepository.save(item);
//        return ItemMapper.createItemDto(item);
//    }
//
//    public ItemWithCommentsAndBookingsDto getItemById(Long itemId, Long ownerId) {
//        User user = handler.getUserFromOpt(ownerId);
//        Item item = handler.getItemFromOpt(itemId);
//        ItemWithCommentsAndBookingsDto itemWithCommentsAndBookingsDto =
//                ItemMapper.createItemWithCommentsAndBookingsDto(item);
//        addCommentsToItem(itemWithCommentsAndBookingsDto);
//        if (item.getOwner().getId().equals(user.getId())) {
//            addBookingsDataToItem(itemWithCommentsAndBookingsDto);
//        }
//        return itemWithCommentsAndBookingsDto;
//    }
//
//    public List<ItemWithCommentsAndBookingsDto> getItemsByOwnerPageable(Long ownerId,
//                                                                        Integer from,
//                                                                        Integer size) {
//        User owner = handler.getUserFromOpt(ownerId);
//        Sort sortByIdAsc = Sort.by(Sort.Direction.ASC, "id");
//        Pageable pageable = PageRequest.of(from, size, sortByIdAsc);
//        List<Item> itemsByOwner = itemRepository.findByOwnerId(owner.getId(), pageable);
//        List<ItemWithCommentsAndBookingsDto> convertedItems = itemsByOwner.stream()
//                .map(ItemMapper::createItemWithCommentsAndBookingsDto)
//                .sorted(Comparator.comparing(ItemWithCommentsAndBookingsDto::getId))
//                .collect(toList());
//        List<ItemWithCommentsAndBookingsDto> itemsWithCommentsAndBookings =
//                new ArrayList<>();
//        for (ItemWithCommentsAndBookingsDto itemWithCommentsAndBookingsDto : convertedItems) {
//            addCommentsToItem(itemWithCommentsAndBookingsDto);
//            addBookingsDataToItem(itemWithCommentsAndBookingsDto);
//            itemsWithCommentsAndBookings.add(itemWithCommentsAndBookingsDto);
//        }
//        return itemsWithCommentsAndBookings;
//    }
//
//    public void deleteItem(Long itemId, Long ownerId) {
//        Item item = handler.getItemFromOpt(itemId);
//        User owner = handler.getUserFromOpt(ownerId);
//        if (item.getOwner().getId().equals(owner.getId())) {
//            itemRepository.deleteById(itemId);
//        }
//    }
//
//    public List<ItemDto> getItemsByQueryPageable(Integer from, Integer size,
//                                                 String text, Long ownerId) {
//        User owner = handler.getUserFromOpt(ownerId);
//        if ((text == null) || (text.isBlank())) {
//            //throw new OperationIsNotSupported("В запросе нет текста");
//            return Collections.EMPTY_LIST;
//        }
//        Sort sortByIdAsc = Sort.by("id").ascending();
//        Pageable pageable = PageRequest.of(from, size, sortByIdAsc);
//        String lowerText = text.toLowerCase();
//        return itemRepository.findItemsByQuery(lowerText, pageable).stream()
//                .filter(item -> isAvailable.test(item))
//                .map(ItemMapper::createItemDto)
//                .collect(toList());
//    }
//
//    public CommentDto createComment(CommentDto commentDto, Long authorId, Long itemId) {
//        if (commentDto.getText().isBlank()) {
//            throw new ValidationException("Пустой текст");
//        }
//        Item item = handler.getItemFromOpt(itemId);
//        User author = handler.getUserFromOpt(authorId);
//        handler.authorValidate(authorId, itemId);
//        Comment comment = CommentMapper.createNewComment(commentDto);
//        comment.setCreated(LocalDateTime.now());
//        comment.setItem(item);
//        comment.setAuthor(author);
//        commentRepository.save(comment);
//        return CommentMapper.createCommentDto(comment);
//    }
//}
