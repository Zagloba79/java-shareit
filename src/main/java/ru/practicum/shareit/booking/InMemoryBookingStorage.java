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

    private final Map<Long, Booking> bookings = new HashMap<>();
    private Long bookingId = 1L;

    @Override
    public Booking create(Booking booking) {
        booking.setId(bookingId++);
        bookings.put(booking.getId(), booking);
        return bookings.get(booking.getId());
    }

    private Optional<Booking> getBookingOpt(Long id) {
        return Optional.of(bookings.get(id));
    }

    @Override
    public Booking getBooking(Long id) {
        return getBookingOpt(id).orElseThrow(() ->
                new ObjectNotFoundException("Данного предмета в базе не существует."));
    }

    @Override
    public void deleteBooking(Long id) {
        bookings.remove(id);
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public List<Booking> getBookingsByBooker(Long bookingId) {
        return findAll().stream()
                .filter(booking -> Objects.equals(booking.getBooker().getId(), bookingId))
                .collect(toList());
    }

    @Override
    public Booking update(Booking booking) {
        bookings.put(booking.getId(), booking);
        return bookings.get(booking.getId());
    }
}