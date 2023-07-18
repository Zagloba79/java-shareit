package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForDataDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.OperationIsNotSupported;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handler.EntityHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.BookingStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EntityHandler handler;

    @Override
    public BookingDto create(NewBookingDto newBookingDto, Long bookerId) {
        LocalDateTime presentTime = LocalDateTime.now();
        handler.bookingValidate(newBookingDto, presentTime);
        Item item = handler.getItemFromOpt(newBookingDto.getItemId());
        handler.itemIsAvailable(item);
        User booker = handler.getUserFromOpt(bookerId);
        if (item.getOwner().getId().equals(bookerId)) {
            throw new OperationIsNotSupported("Букер не может быть владельцем");
        }
        Booking booking = BookingMapper.createNewBooking(
                newBookingDto.getStart(),
                newBookingDto.getEnd(),
                item,
                booker);
        booking.setStatus(WAITING);
        bookingRepository.save(booking);
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        LocalDateTime presentTime = LocalDateTime.now();
        User user = handler.getUserFromOpt(userId);
        Booking booking = handler.getBookingFromOpt(bookingId);
        if (booking.getEnd().isBefore(presentTime)) {
            throw new ValidationException("Время бронирования уже истекло!");
        }
        if (booking.getBooker().getId().equals(userId)) {
            if (!approved) {
                booking.setStatus(CANCELED);
            } else {
                throw new ObjectNotFoundException("Подтвердить бронирование может только владелец вещи!");
            }
        } else if ((booking.getItem().getOwner().getId().equals(user.getId())) &&
                (!booking.getStatus().equals(CANCELED))) {
            if (!booking.getStatus().equals(WAITING)) {
                throw new ValidationException("Решение по бронированию уже принято!");
            }
            if (approved) {
                booking.setStatus(APPROVED);

            } else {
                booking.setStatus(REJECTED);
            }
        } else {
            if (booking.getStatus().equals(CANCELED)) {
                throw new ValidationException("Бронирование было отменено!");
            } else {
                throw new ValidationException("Подтвердить бронирование может только владелец вещи!");
            }
        }
        bookingRepository.save(booking);
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndBookerId(id, userId);
        if (booking == null) {
            booking = bookingRepository.findByIdAndItem_OwnerId(id, userId);
        }
        if (booking == null) {
            throw new ObjectNotFoundException("Вы - левый чувак");
        }
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBookerAndState(String state, Long userId) {
        LocalDateTime presentTime = LocalDateTime.now();
        User user = handler.getUserFromOpt(userId);
        List<Booking> bookings;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBookerId(userId, sortByStartDesc);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndAndStartIsBeforeAndEndIsAfter(
                        user.getId(),
                        presentTime,
                        presentTime,
                        sortByStartDesc);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(
                        userId,
                        presentTime,
                        sortByStartDesc);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(
                        userId,
                        presentTime,
                        sortByStartDesc);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId,
                        WAITING,
                        sortByStartDesc);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId,
                        REJECTED,
                        sortByStartDesc);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwnerAndState(String state, Long userId) {
        LocalDateTime presentTime = LocalDateTime.now();
        User user = handler.getUserFromOpt(userId);
        List<Booking> bookings;
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItem_OwnerId(
                        user.getId(),
                        sortByStartDesc);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(
                        userId,
                        presentTime,
                        presentTime,
                        sortByStartDesc);
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_OwnerIdAndEndIsBefore(
                        userId,
                        presentTime,
                        sortByStartDesc);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItem_OwnerIdAndStartIsAfter(
                        userId,
                        presentTime,
                        sortByStartDesc);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_OwnerIdAndStatus(
                        userId,
                        WAITING,
                        sortByStartDesc);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_OwnerIdAndStatus(
                        userId,
                        REJECTED,
                        sortByStartDesc);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public BookingForDataDto getLastBooking(Long itemId) {
        Booking lastBooking = bookingRepository
                .findFirstByItemIdAndStatusIsAndEndBeforeOrderByEndDesc(
                        itemId,
                        APPROVED,
                        LocalDateTime.now());
        if (lastBooking == null) {
            lastBooking = bookingRepository
                    .findByItemIdAndStatusIsAndStartBeforeAndEndAfter(itemId,
                            APPROVED,
                            LocalDateTime.now(),
                            LocalDateTime.now());
        }
        return BookingMapper.createBookingForDatesDto(lastBooking);
    }

    @Override
    public BookingForDataDto getNextBooking(Long itemId) {
        return BookingMapper.createBookingForDatesDto(
                bookingRepository.findFirstByItemIdAndStatusIsAndStartAfterOrderByStartAsc(
                        itemId,
                        APPROVED,
                        LocalDateTime.now()));
    }

    @Override
    public List<Booking> getBookingsByOwnerId(Long ownerId) {
        Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "start");
        return bookingRepository.findByItem_OwnerId(ownerId, sortByStartDesc);
    }

    @Override
    public List<Booking> getBookingsByItemId(Long itemId) {
        return bookingRepository.findByItemId(itemId);
    }
}