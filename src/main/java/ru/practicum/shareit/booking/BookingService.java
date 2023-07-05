package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {

    BookingDto addNewBooking(BookingDto bookingDto, int bookerId);

    void approveBooking(Booking booking, User owner);

    void rejectBooking(Booking booking, User owner);

    void cancelBooking(Booking booking, User booker);

    List<BookingDto> getBookingByItem(Integer itemId, Integer bookerId);

    List<BookingDto> getBookingsByBooker(Integer bookerId);

    BookingDto update(Integer bookingId, Integer userId, BookingStatus status);

    List<BookingDto> getAllBookings(Integer userId);

    BookingDto getBookingById(Integer bookingId, Integer userId);
}
