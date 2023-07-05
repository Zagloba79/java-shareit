package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.BookingStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public BookingDto addNewBooking(BookingDto bookingDto, Long bookerId) {
        Item item = bookingDto.getItem();
        User booker = userStorage.getUser(bookerId);
        Booking booking = new Booking();
        if (item.getAvailable().equals(false)) {
            log.info("This item has been rented");
        } else {
            booking.setStart(bookingDto.getStart());
            booking.setEnd(bookingDto.getEnd());
            booking.setItem(item);
            booking.setBooker(booker);
            booking.setStatus(WAITING);
            bookingStorage.create(booking);
        }
        Booking bookingFromStorage = bookingStorage.getBooking(booking.getId());
        return BookingMapper.createBookingDto(bookingFromStorage);
    }

    @Override
    public void approveBooking(Booking booking, User owner) {
        Item item = itemStorage.getItem(booking.getItem().getId());
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(APPROVED);
        }
    }

    @Override
    public void rejectBooking(Booking booking, User owner) {
        Item item = itemStorage.getItem(booking.getItem().getId());
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(REJECTED);
        }
    }

    @Override
    public void cancelBooking(Booking booking, User booker) {
        if ((booking.getStatus().equals(WAITING) || booking.getStatus().equals(APPROVED))
                && booking.getBooker().equals(booker)) {
            booking.setStatus(CANCELED);
        }
    }

    @Override
    public List<BookingDto> getBookingByItem(Long itemId, Long bookerId) {
        User booker = userStorage.getUser(bookerId);
        return bookingStorage.findAll().stream()
                .filter(booking -> booking.getItem().getId().equals(itemId))
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public List<BookingDto> getBookingsByBooker(Long bookerId) {
        User booker = userStorage.getUser(bookerId);
        return bookingStorage.getBookingsByBooker(booker.getId()).stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, BookingStatus status) {
        Booking booking = bookingStorage.getBooking(bookingId);
        User user = userStorage.getUser(userId);
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
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookings(Long userId) {
        User user = userStorage.getUser(userId);
        return bookingStorage.findAll().stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        User user = userStorage.getUser(userId);
        Booking booking = bookingStorage.getBooking(bookingId);
        return BookingMapper.createBookingDto(booking);
    }
}