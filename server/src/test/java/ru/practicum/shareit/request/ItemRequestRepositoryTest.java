package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    private final Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
    private User anna = new User("Anna", "anna@mail.ru");
    private User kat = new User("Kat", "kat@mail.ru");

    @BeforeEach
    public void createRequesterAndRequests() {
        anna = userRepository.save(anna);
        kat = userRepository.save(kat);
        ItemRequest itemRequest1 = new ItemRequest("qwerty",
                anna, LocalDateTime.of(2023, 7, 1, 0, 0, 0));
        itemRequestRepository.save(itemRequest1);
        ItemRequest itemRequest2 = new ItemRequest("asdfg",
                anna, LocalDateTime.of(2023, 7, 2, 0, 0, 0));
        itemRequestRepository.save(itemRequest2);
        ItemRequest itemRequest3 = new ItemRequest("qaz",
                kat, LocalDateTime.of(2023, 7, 3, 0, 0, 0));
        itemRequestRepository.save(itemRequest3);
    }

    @AfterEach
    public void cleanDb() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testFindByRequesterId() {
        Long id = anna.getId();
        List<ItemRequest> requests = itemRequestRepository.findByRequesterId(id,
                Sort.by("id").ascending());
        assertEquals(2, requests.size());
        assertEquals("qwerty", requests.get(0).getDescription());
        assertEquals("asdfg", requests.get(1).getDescription());

    }

    @Test
    public void testFindAllByRequesterIdNot() {
        Long id = anna.getId();
        List<ItemRequest> requestsByUser = itemRequestRepository.findAllByRequesterIdNot(id, pageable);
        assertEquals(1, requestsByUser.size());
        assertEquals("qaz", requestsByUser.get(0).getDescription());
    }
}