package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingStorage {
    private Map<Integer, Booking> bookings = new HashMap<>();

    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
}
