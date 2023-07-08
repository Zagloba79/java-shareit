package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.handler.OptionalHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
    public BookingDto addNewBooking(BookingDto bookingDto, Long bookerId) {
        Item item = optionalHandler.getItemFromOpt(bookingDto.getItem().getId());
        User booker = optionalHandler.getUserFromOpt(bookerId);
        Booking booking = new Booking();
        if (item.getAvailable().equals(false)) {
            log.info("This item has been rented");
        } else {
            booking.setStart(bookingDto.getStart());
            booking.setEnd(bookingDto.getEnd());
            booking.setItem(item);
            booking.setBooker(booker);
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

    @Override
    public List<BookingDto> getBookingsDtoByBooker(Long bookerId) {
        User booker = optionalHandler.getUserFromOpt(bookerId);
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getBooker().getId().equals(bookerId))
                .map(BookingMapper::createBookingDto)
                .collect(toList());
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
    public List<BookingDto> getAllBookingsDto(Long userId) {
        User user = optionalHandler.getUserFromOpt(userId);
        return bookingRepository.findAll().stream()
                .map(BookingMapper::createBookingDto)
                .collect(toList());
    }

    @Override
    public BookingDto getBookingDtoById(Long bookingId, Long userId) {
        User user = optionalHandler.getUserFromOpt(userId);
        Booking booking = optionalHandler.getBookingFromOpt(bookingId);
        return BookingMapper.createBookingDto(booking);
    }
}