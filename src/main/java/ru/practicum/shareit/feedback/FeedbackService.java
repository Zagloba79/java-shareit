package ru.practicum.shareit.feedback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import static ru.practicum.shareit.booking.BookingStatus.APPROVED;

@Service
@Slf4j
public class FeedbackService {
    public void addFeedback(Item item, User owner, User booker, String comment) {
        for (Booking booking : item.getBookings().get(booker)) {
            if (booking.getStatuses().contains(APPROVED) && item.isAvailable()) {
                Feedback feedback = new Feedback(item, owner, booker, comment);
                item.getFeedbacks().put(booker, feedback);
            }
        }
    }
}
