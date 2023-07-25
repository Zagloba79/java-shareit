package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.booking.model.BookingStatus.*;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    User booker;
    User otherBooker;
    User owner;
    Item cup;
    Item glass;
    Item pot;
    Booking cupBook;
    Booking glassBook;
    Booking potBook;
    Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
    LocalDateTime presentTime = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        booker = new User("asd", "asd@asd.ru");
        userRepository.save(booker);
        otherBooker = new User("dsa", "dsa@asd.ru");
        userRepository.save(otherBooker);
        owner = new User("boy", "boy@asd.ru");
        userRepository.save(owner);
        cup = new Item("cup", "qwe", true, owner, null);
        itemRepository.save(cup);
        glass = new Item("glass", "qwe", true, owner, null);
        itemRepository.save(glass);
        pot = new Item("pot", "qwe", true, owner, null);
        itemRepository.save(pot);
        cupBook = new Booking(presentTime.minusDays(1), presentTime.plusDays(1),
                cup, booker, WAITING);
        bookingRepository.save(cupBook);
        glassBook = new Booking(presentTime.minusDays(1), presentTime.plusDays(1),
                glass, booker, WAITING);
        bookingRepository.save(glassBook);
        potBook = new Booking(presentTime.minusDays(1), presentTime.plusDays(1),
                pot, otherBooker, WAITING);
        bookingRepository.save(potBook);
    }

    @AfterEach
    public void clearDb() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void findByBookerIdTest() {
        List<Booking> bookings = bookingRepository.findByBookerId(booker.getId(), pageable);
        assertEquals(2, bookings.size());
    }

    @Test
    public void findByBookerIdAndStartIsBeforeAndEndIsAfterTest() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(booker.getId(),
                presentTime, presentTime, pageable);
        assertEquals(2, bookings.size());
    }

    @Test
    public void findByBookerIdAndEndIsBeforeTest() {
        cupBook.setEnd(presentTime.minusHours(1));
        List<Booking> bookings = bookingRepository.findByBookerIdAndEndIsBefore(booker.getId(),
                presentTime, pageable);
        assertEquals(1, bookings.size());
    }

    @Test
    public void findByBookerIdAndStartIsAfterTest() {
        glassBook.setStart(presentTime.plusHours(1));
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsAfter(booker.getId(),
                presentTime, pageable);
        assertEquals(1, bookings.size());
    }

    @Test
    public void findByBookerIdAndStatusTest() {
        glassBook.setStatus(APPROVED);
        List<Booking> bookings = bookingRepository.findByBookerIdAndStatus(booker.getId(),
                APPROVED, pageable);
        assertEquals(1, bookings.size());
    }

    @Test
    public void findByItem_OwnerIdTest() {
        List<Booking> bookings = bookingRepository.findByItem_OwnerId(owner.getId(), pageable);
        assertEquals(3, bookings.size());
    }

    @Test
    public void findByItem_OwnerIdAndStartIsBeforeAndEndIsAfterTest() {
        cupBook.setStart(presentTime.minusHours(5));
        cupBook.setEnd(presentTime.minusHours(1));
        glassBook.setStart(presentTime.minusHours(5));
        glassBook.setEnd(presentTime.plusHours(1));
        potBook.setStart(presentTime.plusHours(5));
        potBook.setEnd(presentTime.plusHours(19));
        List<Booking> bookings = bookingRepository.findByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(
                owner.getId(), presentTime, presentTime, pageable);
        assertEquals(1, bookings.size());
    }

    @Test
    public void findByItem_OwnerIdAndEndIsBeforeTest() {
        cupBook.setStart(presentTime.minusHours(5));
        cupBook.setEnd(presentTime.minusHours(1));
        glassBook.setStart(presentTime.minusHours(5));
        glassBook.setEnd(presentTime.plusHours(1));
        potBook.setStart(presentTime.plusHours(5));
        potBook.setEnd(presentTime.plusHours(19));
        List<Booking> bookings = bookingRepository.findByItem_OwnerIdAndEndIsBefore(
                owner.getId(), presentTime, pageable);
        assertEquals(1, bookings.size());
    }

    @Test
    public void findByItem_OwnerIdAndStartIsAfterTest() {
        cupBook.setStart(presentTime.minusHours(5));
        cupBook.setEnd(presentTime.minusHours(1));
        glassBook.setStart(presentTime.minusHours(5));
        glassBook.setEnd(presentTime.plusHours(1));
        potBook.setStart(presentTime.plusHours(5));
        potBook.setEnd(presentTime.plusHours(19));
        List<Booking> bookings = bookingRepository.findByItem_OwnerIdAndStartIsAfter(
                owner.getId(), presentTime, pageable);
        assertEquals(1, bookings.size());
    }

    @Test
    public void findByItem_OwnerIdAndStatusTest() {
        cupBook.setStatus(APPROVED);
        glassBook.setStatus(REJECTED);
        List<Booking> bookings = bookingRepository.findByItem_OwnerIdAndStatus(
                owner.getId(), REJECTED, pageable);
        assertEquals(1, bookings.size());
    }

    @Test
    public void findByItemIdAndStatusIsAndStartBeforeAndEndAfterTest() {
        cupBook.setStatus(APPROVED);
        cupBook.setStart(presentTime.minusHours(5));
        cupBook.setEnd(presentTime.plusHours(17));
        Booking booking = bookingRepository.findByItemIdAndStatusIsAndStartBeforeAndEndAfter(
                cup.getId(), APPROVED, presentTime, presentTime);
        assertEquals(cup, booking.getItem());
        assertEquals(APPROVED, booking.getStatus());
        assertEquals(booker, booking.getBooker());
    }

    @Test
    public void findFirstByItemIdAndStatusIsAndEndBeforeOrderByEndDescTest() {
        cupBook.setStatus(APPROVED);
        cupBook.setEnd(presentTime.minusHours(17));
        bookingRepository.save(new Booking(presentTime.minusDays(1), presentTime.minusHours(10),
                cup, booker, APPROVED));
        Booking lastBooking = new Booking(presentTime.minusHours(3), presentTime.minusHours(1),
                cup, otherBooker, APPROVED);
        bookingRepository.save(lastBooking);
        bookingRepository.save(new Booking(presentTime.minusDays(3), presentTime.minusDays(2),
                cup, booker, APPROVED));
        Booking booking = bookingRepository.findFirstByItemIdAndStatusIsAndEndBeforeOrderByEndDesc(
                cup.getId(), APPROVED, presentTime);
        assertEquals(cup, booking.getItem());
        assertEquals(APPROVED, booking.getStatus());
        assertEquals(otherBooker, booking.getBooker());
    }

    @Test
    public void findFirstByItemIdAndStatusIsAndStartAfterOrderByStartAscTest() {
        cupBook.setStatus(APPROVED);
        cupBook.setStart(presentTime.plusHours(5));
        cupBook.setEnd(presentTime.plusHours(20));
        bookingRepository.save(new Booking(presentTime.plusDays(8), presentTime.plusDays(10),
                cup, booker, APPROVED));
        Booking nextBooking = new Booking(presentTime.plusHours(3), presentTime.plusHours(4),
                cup, otherBooker, APPROVED);
        bookingRepository.save(nextBooking);
        bookingRepository.save(new Booking(presentTime.plusDays(3), presentTime.plusDays(6),
                cup, booker, APPROVED));
        Booking booking = bookingRepository.findFirstByItemIdAndStatusIsAndStartAfterOrderByStartAsc(
                cup.getId(), APPROVED, presentTime);
        assertEquals(cup, booking.getItem());
        assertEquals(APPROVED, booking.getStatus());
        assertEquals(otherBooker, booking.getBooker());
    }

    @Test
    public void findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatusTest() {
        cupBook.setStart(presentTime.minusHours(23));
        cupBook.setEnd(presentTime.minusHours(20));
        bookingRepository.save(new Booking(presentTime.minusDays(8), presentTime.minusDays(6),
                cup, otherBooker, APPROVED));
        bookingRepository.save(new Booking(presentTime.minusDays(7), presentTime.minusDays(6),
                pot, otherBooker, APPROVED));
        bookingRepository.save(new Booking(presentTime.plusDays(3), presentTime.plusDays(6),
                cup, otherBooker, APPROVED));
        bookingRepository.save(new Booking(presentTime.minusDays(2), presentTime.minusDays(1),
                cup, booker, APPROVED));
        Booking booking = bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(
                cup.getId(), booker.getId(), presentTime, APPROVED);
        assertEquals(cup, booking.getItem());
        assertEquals(APPROVED, booking.getStatus());
        assertEquals(booker, booking.getBooker());
        assertEquals(presentTime.minusDays(2), booking.getStart());
    }

    @Test
    public void findByIdAndBookerIdTest() {
        Booking booking = bookingRepository.findByIdAndBookerId(cupBook.getId(), booker.getId());
        assertEquals(cup, booking.getItem());
        assertEquals(WAITING, booking.getStatus());
        assertEquals(booker, booking.getBooker());
    }

    @Test
    public void findByIdAndItem_OwnerIdTest() {
        Optional<Booking> bookingOpt = bookingRepository.findByIdAndItem_OwnerId(
                cupBook.getId(), owner.getId());
        Booking booking = new Booking();
        if (bookingOpt.isPresent()) {
            booking = bookingOpt.get();
        }
        assertEquals(cup, booking.getItem());
        assertEquals(WAITING, booking.getStatus());
        assertEquals(booker, booking.getBooker());
    }
}
