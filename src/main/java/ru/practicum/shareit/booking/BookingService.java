package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.BookingStatus.*;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class BookingService {
    private InMemoryBookingStorage bookingStorage;
    private UserStorage userStorage;
    private BookingMapper bookingMapper;
    private ItemStorage itemStorage;
    private int ids = 0;

    public BookingDto addNewBooking(BookingDto bookingDto, int bookerId) {
        int itemId = bookingDto.getItemId();
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        Booking booking = new Booking();
        if (!item.isAvailable()) {
            log.info("This item has been rented");
        } else {
            booking.setId(ids++);
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


    private Boolean validateBooking(Booking booking) {
        return booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())
                && booking.getStatus().equals(APPROVED);
    }

    public List<BookingDto> getBookingByItem(int itemId) {
        return bookingStorage.findAll().stream()
                .filter(booking -> booking.getItemId() == itemId)
                .map(bookingMapper::createBookingDto)
                .collect(toList());
    }

    public List<BookingDto> getBookingsByBooker(int bookerId) {
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        return bookingStorage.getBookingsByBooker(booker.getId()).stream()
                .map(bookingMapper::createBookingDto)
                .collect(toList());
    }


    public BookingDto update(int bookingId, int userId, BookingStatus status) {
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

    public List<BookingDto> getAllBookings() {
        return bookingStorage.findAll().stream()
                .map(bookingMapper::createBookingDto)
                .collect(toList());
    }

    public BookingDto getBookingById(int bookingId) {
        Booking booking = bookingStorage.getBookingById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Резерва с " + bookingId + " не существует."));
        return bookingMapper.createBookingDto(booking);
    }
}