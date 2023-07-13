package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handler.OptionalHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.NearestBookings;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.BookingStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final OptionalHandler optionalHandler;
    private final BookingRepository bookingRepository;


    @Override
    public BookingDto create(NewBookingDto bookingDto, Long bookerId) {
        LocalDateTime presentTime = LocalDateTime.now().minusMinutes(10);
        User booker = optionalHandler.getUserFromOpt(bookerId);
        Long itemId = bookingDto.getItemId();
        Item item = optionalHandler.getItemFromOpt(itemId);
        itemValidate(item);
        bookingValidate(bookingDto, presentTime);
        Booking booking = BookingMapper.createNewBooking(
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker);
        booking.setStatus(WAITING);
        bookingRepository.save(booking);
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public void approveBooking(Booking booking, User owner) {
        Item item = optionalHandler.getItemFromOpt(booking.getItem().getId());
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(APPROVED);
        }
    }

    @Override
    public void rejectBooking(Booking booking, User owner) {
        Item item = optionalHandler.getItemFromOpt(booking.getItem().getId());
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(REJECTED);
        }
    }

    @Override
    public void cancelBooking(Booking booking, User booker) {
        Item item = optionalHandler.getItemFromOpt(booking.getItem().getId());
        if ((booking.getStatus().equals(WAITING) || booking.getStatus().equals(APPROVED))
                && booking.getBooker().equals(booker)) {
            booking.setStatus(CANCELED);
        }
    }

    @Override
    public List<BookingDto> getBookingsDtoByItem(Long itemId, Long bookerId) {
        User booker = optionalHandler.getUserFromOpt(bookerId);
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getItem().getId().equals(itemId))
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public NearestBookings getBookingsBeforeAndAfterNow(Long itemId, Long ownerId) {
        LocalDateTime presentTime = LocalDateTime.now();
        NearestBookings nearestBookings = new NearestBookings();
        List<BookingDto> bookings = getBookingsDtoByItem(itemId, ownerId);
        List<BookingDto> bookingsBefore = bookings.stream()
                .filter(booking -> booking.getEnd().isBefore(presentTime))
                .sorted((b1, b2) -> b2.getEnd().compareTo(b1.getEnd()))
                .collect(toList());
        nearestBookings.setPreviousBookingStart(bookingsBefore.get(0).getStart());
        nearestBookings.setPreviousBookingEnd(bookingsBefore.get(0).getEnd());
        List<BookingDto> bookingsAfter = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(presentTime))
                .sorted(Comparator.comparing(BookingDto::getStart))
                .collect(toList());
        nearestBookings.setNextBookingStart(bookingsAfter.get(0).getStart());
        nearestBookings.setNextBookingEnd(bookingsAfter.get(0).getEnd());
        return nearestBookings;
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        Booking booking = optionalHandler.getBookingFromOpt(bookingId);
        if (booking.getStatus().equals(APPROVED)) {
            throw new ValidationException("Booking is approved");
        }
        User user = optionalHandler.getUserFromOpt(userId);
        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (approved) {
                approveBooking(booking, user);
            } else {
                rejectBooking(booking, user);
            }
        } else if (booking.getBooker().getId().equals(userId)) {
            if (approved) {
                cancelBooking(booking, user);
            } else {
                throw new ObjectNotFoundException("Левый чувак");
            }
        } else {
            throw new ValidationException("Левый чувак");
        }
        bookingRepository.save(booking);
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public BookingDto getBookingDtoById(Long bookingId, Long userId) {
        User user = optionalHandler.getUserFromOpt(userId);
        Booking booking = optionalHandler.getBookingFromOpt(bookingId);
        if (!booking.getBooker().getId().equals(user.getId()) &&
                !booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new ObjectNotFoundException("Левый чувак");
        }
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsDtoByBookerAndState(String state, Long bookerId) {
        User booker = optionalHandler.getUserFromOpt(bookerId);
        List<BookingDto> listByUser = bookingRepository.findAll().stream()
                .filter(booking -> booking.getBooker().getId().equals(bookerId))
                .map(BookingMapper::createBookingDto)
                .collect(toList());
        return filterByStateAndSortByStart(state, listByUser);
    }

    @Override
    public List<BookingDto> getBookingsDtoByOwnerAndState(String state, Long ownerId) {
        User owner = optionalHandler.getUserFromOpt(ownerId);
        List<BookingDto> listByUser = bookingRepository.findAll().stream()
                .filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                .map(BookingMapper::createBookingDto)
                .collect(toList());
        return filterByStateAndSortByStart(state, listByUser);
    }

    @Override
    public List<BookingDto> getBookingsDtoByUserAndState(String state, Long userId) {
        List<BookingDto> listByBooker = getBookingsDtoByBookerAndState(state, userId);
        List<BookingDto> listByOwner = getBookingsDtoByOwnerAndState(state, userId);
        List<BookingDto> list = new ArrayList<>(listByBooker);
        list.addAll(listByOwner);
        return filterByStateAndSortByStart(state, list);
    }

    private List<BookingDto> filterByStateAndSortByStart(String state, List<BookingDto> listByUser) {
        List<BookingDto> bookings;
        switch (state) {
            case "ALL":
                bookings = listByUser;
                break;
            case "CURRENT":
                bookings = listByUser.stream()
                        .filter(bookingDto -> bookingDto.getStatus().equals(APPROVED) &&
                                (bookingDto.getStart().isBefore(LocalDateTime.now()) &&
                                        bookingDto.getEnd().isAfter(LocalDateTime.now())))
                        .collect(toList());
                break;
            case "PAST":
                bookings = listByUser.stream()
                        .filter(bookingDto -> bookingDto.getStatus().equals(APPROVED) &&
                                bookingDto.getEnd().isBefore(LocalDateTime.now()))
                        .collect(toList());
                break;
            case "FUTURE":
                bookings = listByUser.stream()
                        .filter(bookingDto -> bookingDto.getStatus().equals(APPROVED) &&
                                bookingDto.getStart().isAfter(LocalDateTime.now()))
                        .collect(toList());
                break;
            case "WAITING":
                bookings = listByUser.stream()
                        .filter(bookingDto -> bookingDto.getStatus().equals(WAITING))
                        .collect(toList());
                break;
            case "REJECTED":
                bookings = listByUser.stream()
                        .filter(bookingDto -> bookingDto.getStatus().equals(REJECTED))
                        .collect(toList());
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        bookings.sort((b1, b2) -> b2.getStart().compareTo(b1.getStart()));
        return bookings;
    }

    private void itemValidate(Item item) {
        if (item.getAvailable() == null || !item.getAvailable()) {
            throw new ValidationException("Некорректный статус предмета: ");
        }
    }

    private void bookingValidate(NewBookingDto bookingDto, LocalDateTime presentTime) {
        if (bookingDto.getStart() == null ||
                bookingDto.getEnd() == null) {
            throw new ValidationException("даты не заполнены");
        }
        if (bookingDto.getStart().isBefore(presentTime) ||
                bookingDto.getEnd().isBefore(presentTime)) {
            throw new ValidationException("даты в прошлом");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) ||
                bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("даты попутаны");
        }
    }
}