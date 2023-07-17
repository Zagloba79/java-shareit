package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {
    public static BookingDto createBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static BookingForDatesDto createBookingForDatesDto(Booking booking) {
        BookingForDatesDto bookingForDatesDto = new BookingForDatesDto();
        if (booking != null) {
            bookingForDatesDto.setId(booking.getId());
            bookingForDatesDto.setStart(booking.getStart());
            bookingForDatesDto.setEnd(booking.getEnd());
            bookingForDatesDto.setBookerId(booking.getBooker().getId());
            return bookingForDatesDto;
        } else {
            return null;
        }
    }

    public static Booking createNewBooking(LocalDateTime start, LocalDateTime end, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        return booking;
    }
}