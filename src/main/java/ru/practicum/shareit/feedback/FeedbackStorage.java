package ru.practicum.shareit.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.booking.BookingStatus.APPROVED;

@Data
@AllArgsConstructor
public class FeedbackStorage {
    private Map<Item, Feedback> feedbacks = new HashMap<>();

    public void add(Item item, User owner, User booker, String comment) {
        for (Booking booking : item.getBookings().get(booker)) {
            if (booking.getStatuses().contains(APPROVED) && item.isAvailable()) {
                Feedback feedback = new Feedback(item, owner, booker, comment);
                item.getFeedbacks().put(booker, feedback);
            }
        }
    }

    public List<Feedback> findAll() {
        return new ArrayList<>(feedbacks.values());
    }
}
