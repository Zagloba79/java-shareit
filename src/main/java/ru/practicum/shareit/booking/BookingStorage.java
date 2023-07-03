package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingStorage {
    Booking create(Booking booking);

    List<Booking> findAll();

    Booking getBooking(Integer bookingId);

    void deleteBooking(int itemId);

    List<Booking> getBookingsByBooker(int bookingId);

    Booking update(Booking booking);
}