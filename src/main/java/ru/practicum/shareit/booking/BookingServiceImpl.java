package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.OperationIsNotSupported;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.BookingStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final EntityHandler handler;

    @Override
    @Transactional
    public BookingDto create(NewBookingDto newBookingDto, Long bookerId) {
        handler.bookingValidate(newBookingDto, LocalDateTime.now());
        Item item = handler.getItemFromOpt(newBookingDto.getItemId());
        handler.itemIsAvailable(item);
        User booker = handler.getUserFromOpt(bookerId);
        if (item.getOwner().getId().equals(bookerId)) {
            throw new OperationIsNotSupported("Владелец не может быть букером");
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
    @Transactional
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        User owner = handler.getUserFromOpt(userId);
        Booking booking = handler.getBookingByIdAndOwnerIdFromOpt(bookingId, owner.getId());
        validateBooking(booking);
        booking.setStatus(approved ? APPROVED : REJECTED);
        bookingRepository.save(booking);
        return BookingMapper.createBookingDto(booking);
    }

    private void validateBooking(Booking booking) {
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время бронирования уже истекло!");
        }
        if (!booking.getStatus().equals(WAITING)) {
            throw new ValidationException("Решение по бронированию уже принято!");
        }
    }

    @Override
    public BookingDto getBookingById(Long id, Long userId) {
        Booking booking = bookingRepository.findByIdAndBookerId(id, userId);
        if (booking == null) {
            booking = bookingRepository.findByIdAndItem_OwnerId(id, userId).orElseThrow(() ->
                    new OperationIsNotSupported("Вы - левый чувак"));
        }
        return BookingMapper.createBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBookerAndState(Integer from, Integer size,
                                                        String state, Long userId) {
        LocalDateTime presentTime = LocalDateTime.now();
        User user = handler.getUserFromOpt(userId);
        List<Booking> bookings;
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size,
                Sort.by("start").descending());
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBookerId(
                        userId,
                        pageable);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(
                        user.getId(),
                        presentTime,
                        presentTime,
                        pageable);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(
                        userId,
                        presentTime,
                        pageable);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(
                        userId,
                        presentTime,
                        pageable);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId,
                        WAITING,
                        pageable);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatus(
                        userId,
                        REJECTED,
                        pageable);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwnerAndState(Integer from, Integer size,
                                                       String state, Long userId) {
        LocalDateTime presentTime = LocalDateTime.now();
        User user = handler.getUserFromOpt(userId);
        List<Booking> bookings;
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size,
                Sort.by("start").descending());
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItem_OwnerId(
                        user.getId(),
                        pageable);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItem_OwnerIdAndStartIsBeforeAndEndIsAfter(
                        userId,
                        presentTime,
                        presentTime,
                        pageable);
                break;
            case "PAST":
                bookings = bookingRepository.findByItem_OwnerIdAndEndIsBefore(
                        userId,
                        presentTime,
                        pageable);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItem_OwnerIdAndStartIsAfter(
                        userId,
                        presentTime,
                        pageable);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItem_OwnerIdAndStatus(
                        userId,
                        WAITING,
                        pageable);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItem_OwnerIdAndStatus(
                        userId,
                        REJECTED,
                        pageable);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }
}