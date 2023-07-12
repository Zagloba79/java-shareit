package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handler.OptionalHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.NearestBookings;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
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
        Item item = optionalHandler.getItemFromOpt(bookingDto.getItem().getId());
        User booker = optionalHandler.getUserFromOpt(bookerId);
        Booking booking = new Booking();
        if (item.getAvailable().equals(false)) {
            log.info("This item has been rented");
        } else {
            BookingMapper.createNewBooking(bookingDto, booker);
            booking.setStatus(WAITING);
            bookingRepository.save(booking);
        }
        Booking bookingFromRepository = optionalHandler.getBookingFromOpt(booking.getId());
        return BookingMapper.createBookingDto(bookingFromRepository);
    }

    @Override
    public void approveBooking(Booking booking, Long ownerId) {
        Item item = optionalHandler.getItemFromOpt(booking.getItem().getId());
        User owner = optionalHandler.getUserFromOpt(ownerId);
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(APPROVED);
        }
    }

    @Override
    public void rejectBooking(Booking booking, Long ownerId) {
        Item item = optionalHandler.getItemFromOpt(booking.getItem().getId());
        User owner = optionalHandler.getUserFromOpt(ownerId);
        if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
            booking.setStatus(REJECTED);
        }
    }

    @Override
    public void cancelBooking(Booking booking, Long bookerId) {
        Item item = optionalHandler.getItemFromOpt(booking.getItem().getId());
        User booker = optionalHandler.getUserFromOpt(bookerId);
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

//    @Override
//    public NearestBookings getBookingsBeforeAndAfterNow(Long itemId, Long ownerId) {
//        LocalDateTime presentTime = LocalDateTime.now();
//        NearestBookings neededBookings = new NearestBookings();
//        Booking previousBooking = bookingRepository
//                .findFirstByItem_IdAndEndBeforeOrderByEndDesc(itemId, presentTime);
//        neededBookings.setPreviousBookingStart(previousBooking.getStart());
//        neededBookings.setPreviousBookingEnd(previousBooking.getEnd());
//        Booking nextBooking = bookingRepository
//                .findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId, presentTime);
//        neededBookings.setNextBookingStart(nextBooking.getStart());
//        neededBookings.setNextBookingEnd(nextBooking.getEnd());
//        return neededBookings;
//    }

    @Override
    public NearestBookings getBookingsBeforeAndAfterNow(Long itemId, Long ownerId) {
        LocalDateTime timeIsNow = LocalDateTime.now();
        NearestBookings neededBookings = new NearestBookings();
        List<BookingDto> bookings = getBookingsDtoByItem(itemId, ownerId);
        List<BookingDto> bookingsBefore = bookings.stream()
                .filter(booking -> booking.getEnd().isBefore(timeIsNow))
                .sorted((b1, b2) -> b2.getEnd().compareTo(b1.getEnd()))
                .collect(toList());
        neededBookings.setPreviousBookingStart(bookingsBefore.get(0).getStart());
        neededBookings.setPreviousBookingEnd(bookingsBefore.get(0).getEnd());
        List<BookingDto> bookingsAfter = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(timeIsNow))
                .sorted(Comparator.comparing(BookingDto::getStart))
                .collect(toList());
        neededBookings.setNextBookingStart(bookingsAfter.get(0).getStart());
        neededBookings.setNextBookingEnd(bookingsAfter.get(0).getEnd());
        return neededBookings;
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, boolean approved) {
        Booking booking = optionalHandler.getBookingFromOpt(bookingId);
        User user = optionalHandler.getUserFromOpt(userId);
        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (approved) {
                approveBooking(booking, userId);
            } else {
                rejectBooking(booking, userId);
            }
        } else if (booking.getBooker().getId().equals(userId)) {
            cancelBooking(booking, userId);
        } else {
            throw new ValidationException("Левый чувак");
        }
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public BookingDto getBookingDtoById(Long bookingId, Long userId) {
        User user = optionalHandler.getUserFromOpt(userId);
        Booking booking = optionalHandler.getBookingFromOpt(bookingId);
        if (!booking.getBooker().getId().equals(user.getId()) ||
                !booking.getItem().getOwner().getId().equals(user.getId())) {
            throw new ValidationException("Левый чувак");
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
        return sortedByState(state, listByUser);
    }

    @Override
    public List<BookingDto> getBookingsDtoByOwnerAndState(String state, Long ownerId) {
        User booker = optionalHandler.getUserFromOpt(ownerId);
        List<BookingDto> listByUser = bookingRepository.findAll().stream()
                .filter(booking -> booking.getItem().getOwner().getId().equals(ownerId))
                .map(BookingMapper::createBookingDto)
                .collect(toList());
        return sortedByState(state, listByUser);
    }


    private List<BookingDto> sortedByState(String state, List<BookingDto> listByUser) {
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
                throw new ValidationException("Неизвестное нам слово - " + state);
        }
        bookings.sort((b1, b2) -> b2.getStart().compareTo(b1.getStart()));
        return bookings;
    }
}