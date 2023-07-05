package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {

    BookingDto addNewBooking(BookingDto bookingDto, Long bookerId);

    void approveBooking(Booking booking, User owner);

    void rejectBooking(Booking booking, User owner);

    void cancelBooking(Booking booking, User booker);

    List<BookingDto> getBookingByItem(Long itemId, Long bookerId);

    List<BookingDto> getBookingsByBooker(Long bookerId);

    BookingDto update(Long bookingId, Long userId, BookingStatus status);

    List<BookingDto> getAllBookings(Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);
}
