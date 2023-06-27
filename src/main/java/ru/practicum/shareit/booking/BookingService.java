package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.BookingStatus.*;

@Service
@Slf4j
public class BookingService {
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final BookingMapper bookingMapper;
    private final ItemStorage itemStorage;
    private Integer bookingId = 1;

    public BookingService(BookingStorage bookingStorage, UserStorage userStorage,
                          BookingMapper bookingMapper, ItemStorage itemStorage) {
        this.bookingStorage = bookingStorage;
        this.userStorage = userStorage;
        this.bookingMapper = bookingMapper;
        this.itemStorage = itemStorage;
    }

    public BookingDto addNewBooking(BookingDto bookingDto, int bookerId) {
        int itemId = bookingDto.getItemId();
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        Booking booking = new Booking();
        if (item.getAvailable().equals(false)) {
            log.info("This item has been rented");
        } else {
            booking.setId(bookingId++);
            booking.setStart(bookingDto.getStart());
            booking.setEnd(bookingDto.getEnd());
            booking.setItemId(itemId);
            booking.setBooker(booker);
            booking.setStatus(WAITING);
            ArrayList<BookingStatus> statuses = new ArrayList<>();
            statuses.add(WAITING);
            booking.setStatuses(statuses);
            bookingStorage.create(booking);
        }
        if (booking.getId() == null) {
            booking.setId(bookingId++);
        }
        Booking bookingFromStorage = bookingStorage.getBookingById(booking.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
        return bookingMapper.createBookingDto(bookingFromStorage);
    }

    public void approveBooking(Booking booking, User owner) {
        Item item = itemStorage.getItemById(booking.getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(APPROVED);
            booking.getStatuses().add(APPROVED);
        }
    }

    public void rejectBooking(Booking booking, User owner) {
        Item item = itemStorage.getItemById(booking.getItemId()).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(REJECTED);
            booking.getStatuses().add(REJECTED);
        }
    }


    public void cancelBooking(Booking booking, User booker) {
        if ((booking.getStatus().equals(WAITING) || booking.getStatus().equals(APPROVED))
                && booking.getBooker().equals(booker)) {
            booking.setStatus(CANCELED);
            booking.getStatuses().add(CANCELED);
        }
    }

    public List<BookingDto> getBookingByItem(Integer itemId, Integer bookerId) {
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        return bookingStorage.findAll().stream()
                .filter(booking -> booking.getItemId().equals(itemId))
                .map(bookingMapper::createBookingDto)
                .collect(toList());
    }

    public List<BookingDto> getBookingsByBooker(Integer bookerId) {
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        return bookingStorage.getBookingsByBooker(booker.getId()).stream()
                .map(bookingMapper::createBookingDto)
                .collect(toList());
    }


    public BookingDto update(Integer bookingId, Integer userId, BookingStatus status) {
        Booking booking = bookingStorage.getBookingById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Резерва с " + bookingId + " не существует."));
        User user = userStorage.getUser(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userId + " не существует."));
        switch (status) {
            case APPROVED:
                approveBooking(booking, user);
                break;
            case REJECTED:
                rejectBooking(booking, user);
                break;
            case CANCELED:
                cancelBooking(booking, user);
                break;
        }
        return bookingMapper.createBookingDto(booking);
    }

    public List<BookingDto> getAllBookings(Integer userId) {
        User user = userStorage.getUser(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userId + " не существует."));
        return bookingStorage.findAll().stream()
                .map(bookingMapper::createBookingDto)
                .collect(toList());
    }

    public BookingDto getBookingById(Integer bookingId, Integer userId) {
        User user = userStorage.getUser(userId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + userId + " не существует."));
        Booking booking = bookingStorage.getBookingById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Резерва с " + bookingId + " не существует."));
        return bookingMapper.createBookingDto(booking);
    }
}