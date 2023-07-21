package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

public interface BookingService {

    BookingDto create(NewBookingDto bookingDto, Long bookerId);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookingsByBookerAndState(Integer from, Integer size,
                                                 String state, Long userId);

    List<BookingDto> getBookingsByOwnerAndState(Integer from, Integer size,
                                                String state, Long userId);
}