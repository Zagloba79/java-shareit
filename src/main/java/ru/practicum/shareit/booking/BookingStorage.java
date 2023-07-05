package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingStorage {
    Booking create(Booking booking);

    List<Booking> findAll();

    Booking getBooking(Long bookingId);

    void deleteBooking(Long itemId);

    List<Booking> getBookingsByBooker(Long bookingId);

    Booking update(Booking booking);
}