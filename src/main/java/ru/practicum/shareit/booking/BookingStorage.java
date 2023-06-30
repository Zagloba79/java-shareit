package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingStorage {
    Booking create(Booking booking);

    List<Booking> findAll();

    Optional<Booking> getBookingById(int bookingId);

    void deleteBooking(int itemId);

    List<Booking> getBookingsByBooker(int bookingId);

    Booking update(Booking booking);
}