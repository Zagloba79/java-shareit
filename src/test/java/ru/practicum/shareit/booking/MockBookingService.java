package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

public class MockBookingService implements BookingService {
    @Override
    public BookingDto create(NewBookingDto newBookingDto, Long bookerId) {
        return null;
    }

    @Override
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        return null;
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingsByBookerAndState(Integer from, Integer size,
                                                        String state, Long userId) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingsByOwnerAndState(Integer from, Integer size,
                                                       String state, Long userId) {
        return null;
    }
}
