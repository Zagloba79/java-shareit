package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.NearestBookings;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {

    BookingDto create(NewBookingDto bookingDto, Long bookerId);

    void approveBooking(Booking booking, User owner);

    void rejectBooking(Booking booking, User owner);

    void cancelBooking(Booking booking, User booker);

    List<BookingDto> getBookingsDtoByItem(Long itemId, Long bookerId);

    NearestBookings getBookingsBeforeAndAfterNow(Long itemId, Long bookerId);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingDtoById(Long bookingId, Long userId);

    List<BookingDto> getBookingsDtoByBookerAndState(String state, Long userId);

    List<BookingDto> getBookingsDtoByOwnerAndState(String state, Long userId);

    List<BookingDto> getBookingsDtoByUserAndState(String state, Long userId);
}
