package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDto addNewBooking(BookingDto bookingDto, Long bookerId);

    void approveBooking(Booking booking, Long ownerId);

    void rejectBooking(Booking booking, Long ownerId);

    void cancelBooking(Booking booking, Long bookerId);

    List<BookingDto> getBookingsDtoByItem(Long itemId, Long bookerId);

    BookingDto update(Long bookingId, Long userId, boolean approved);

    BookingDto getBookingDtoById(Long bookingId, Long userId);

    List<BookingDto> getBookingsDtoByBookerAndState(String state, Long userId);

    List<BookingDto> getBookingsDtoByOwnerAndState(String state, Long userId);
}
