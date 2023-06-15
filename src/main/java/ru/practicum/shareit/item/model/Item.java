package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.feedback.Feedback;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO Sprint add-controllers.
 */
@Data
@NoArgsConstructor
public class Item {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private String request;
    private HashMap<User, ArrayList<Booking>> bookings;
    private HashMap<User, Feedback> feedbacks;

    public Item(String name, String description, boolean available, User owner, String request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}
