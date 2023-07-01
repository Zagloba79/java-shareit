package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component("bookingStorage")
@RequiredArgsConstructor
public class InMemoryBookingStorage implements BookingStorage {

    private final Map<Integer, Booking> bookings = new HashMap<>();
    private Integer bookingId = 1;

    @Override
    public Booking create(Booking booking) {
        booking.setId(bookingId++);
        bookings.put(booking.getId(), booking);
        return bookings.get(booking.getId());
    }

    private Optional<Booking> getBookingOpt(Integer id) {
        return Optional.of(bookings.get(id));
    }

    public Booking getBooking(Integer id) {
        return getBookingOpt(id).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
    }

    @Override
    public void deleteBooking(int id) {
        bookings.remove(id);
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public List<Booking> getBookingsByBooker(int bookingId) {
        return findAll().stream()
                .filter(booking -> booking.getBooker().getId() == bookingId)
                .collect(toList());
    }

    @Override
    public Booking update(Booking booking) {
        bookings.put(booking.getId(), booking);
        return bookings.get(booking.getId());
    }
}