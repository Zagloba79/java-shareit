package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

public final class BookingMapper {
    private BookingMapper() {
    }

    public static BookingDto createBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus(),
                booking.getStatuses());
    }

    public static Booking createBooking(BookingDto bookingDto, User booker) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem(),
                booker,
                bookingDto.getStatus(),
                bookingDto.getStatuses());
    }
}