package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private User owner = new User(1L, "Anna", "anna@mail.ru");
    Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

    @AfterEach
    public void clearDb() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    public void findByOwnerIdTest() {
        owner = userRepository.save(owner);
        Item item1 = new Item(1L, "item1", "description1",
                true, owner, null);
        itemRepository.save(item1);
        Item item2 = new Item(2L, "item2", "description2",
                true, owner, null);
        itemRepository.save(item2);
        Long id = owner.getId();
        List<Item> itemsByOwner = itemRepository.findByOwnerId(id, pageable);
        assertEquals(2, itemsByOwner.size());
    }

    @Test
    public void getItemsByQueryTest() {
        owner = userRepository.save(owner);
        Item workTable = new Item(2L, "workTable", "Рабочий стол",
                true, owner, null);
        itemRepository.save(workTable);
        Item tableTop = new Item(1L, "tableTop", "Столешница",
                true, owner, null);
        itemRepository.save(tableTop);
//        Item fork = new Item(null, "fork", "вилка",
//                true, owner, null);
//        itemRepository.save(fork);
//        Item pokerTable = new Item(null, "Стол для покера", "Большой стол для покера",
//                true, owner, null);
//        itemRepository.save(pokerTable);
//        Item lamp = new Item(null, "Лампа", "Настольная лампа",
//                true, owner, null);
//        itemRepository.save(lamp);
//        Item dinningTable = new Item(null, "Обеденный стол", "description1",
//                true, owner, null);
//        itemRepository.save(dinningTable);
        List<Item> itemsByQuery = itemRepository.findItemsByQuery("стол", pageable);
        assertEquals(2, itemsByQuery.size());
    }

    @Test
    public void findAllByRequestIdInTest() {
        owner = userRepository.save(owner);
        ItemRequest request = new ItemRequest(1L, "Хочу купить слона",
                owner, LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        itemRequestRepository.save(request);
        ItemRequest otherRequest = new ItemRequest(2L, "Ничего не надо",
                owner, LocalDateTime.of(2023, 7, 2, 0, 0, 1));
        itemRequestRepository.save(otherRequest);
        Item homePhone = new Item(8L, "homePhone", "description1",
                true, owner, request);
        itemRepository.save(homePhone);
        Item mobilePhone = new Item(9L, "mobilePhone", "new mobilePhone Alcatel",
                true, owner, request);
        itemRepository.save(mobilePhone);
        Item router = new Item(10L, "router", "new router Alcatel",
                true, owner, null);
        itemRepository.save(router);
        Item box = new Item(11L, "box", "empty box",
                true, owner, otherRequest);
        itemRepository.save(box);
        List<Item> itemsByRequestId = itemRepository.findAllByRequestIdIn(
                List.of(1L), Sort.by( "id").ascending());
        assertEquals(2, itemsByRequestId.size());
    }
}



//    List<Item> findAllByRequestIdIn(List<Long> ids, Sort sort);