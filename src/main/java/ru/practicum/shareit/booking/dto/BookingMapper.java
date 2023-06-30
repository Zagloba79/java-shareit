package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

public final class BookingMapper {
    private BookingMapper() {
    }

    public static BookingDto createBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static Booking createBooking(BookingDto bookingDto, User booker) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(bookingDto.getItem());
        booking.setBooker(booker);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }
}