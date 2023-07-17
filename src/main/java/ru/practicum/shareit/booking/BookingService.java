package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForDatesDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDto create(NewBookingDto bookingDto, Long bookerId);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookingsByBookerAndState(String state, Long userId);

    List<BookingDto> getBookingsByOwnerAndState(String state, Long userId);

    BookingForDatesDto getPreviousBooking(Long itemId);

    BookingForDatesDto getNextBooking(Long itemId);

    List<Booking> getBookingsByOwnerId(Long ownerId);

    List<Booking> getBookingsByItemId(Long itemId);
}