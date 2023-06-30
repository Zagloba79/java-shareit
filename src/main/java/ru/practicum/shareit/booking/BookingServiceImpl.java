package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.BookingStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDto addNewBooking(BookingDto bookingDto, int bookerId) {
        Item item = bookingDto.getItem();
        User booker = userService.userFromStorage(bookerId);
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
        Booking bookingFromStorage = bookingStorage.getBookingById(booking.getId()).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
        return BookingMapper.createBookingDto(bookingFromStorage);
    }

    @Override
    public void approveBooking(Booking booking, User owner) {
        Item item = itemService.itemFromStorage(booking.getItem().getId());
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(APPROVED);
        }
    }

    @Override
    public void rejectBooking(Booking booking, User owner) {
        Item item = itemService.itemFromStorage(booking.getItem().getId());
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
    public List<BookingDto> getBookingByItem(Integer itemId, Integer bookerId) {
        User booker = userService.userFromStorage(bookerId);
        return bookingStorage.findAll().stream()
                .filter(booking -> booking.getItem().getId().equals(itemId))
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public List<BookingDto> getBookingsByBooker(Integer bookerId) {
        User booker = userService.userFromStorage(bookerId);
        return bookingStorage.getBookingsByBooker(booker.getId()).stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }


    @Override
    public BookingDto update(Integer bookingId, Integer userId, BookingStatus status) {
        Booking booking = bookingFromStorage(bookingId);
        User user = userService.userFromStorage(userId);
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
    public List<BookingDto> getAllBookings(Integer userId) {
        User user = userService.userFromStorage(userId);
        return bookingStorage.findAll().stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public BookingDto getBookingById(Integer bookingId, Integer userId) {
        User user = userService.userFromStorage(userId);
        Booking booking = bookingFromStorage(bookingId);
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public Booking bookingFromStorage(Integer bookingId) {
        return bookingStorage.getBookingById(bookingId).orElseThrow(() ->
                new ObjectNotFoundException("Резерва с " + bookingId + " не существует."));
    }
}