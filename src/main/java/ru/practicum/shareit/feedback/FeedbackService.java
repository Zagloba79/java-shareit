package ru.practicum.shareit.feedback;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.dto.FeedbackMapper;
import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.InMemoryUserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;

@Service
@AllArgsConstructor
public class FeedbackService {
    private FeedbackStorage feedbackStorage;
    private BookingStorage bookingStorage;
    private ItemStorage itemStorage;
    private FeedbackMapper feedbackMapper;
    private InMemoryUserStorage userStorage;
    private static Integer feedbackId = 1;


    public FeedbackDto createFeedback(FeedbackDto feedbackDto, Integer bookerId, Integer itemId) {
        Feedback feedback = new Feedback();
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        for (Booking booking : bookingStorage.getBookingsByBooker(bookerId)) {
            if (booking.getItemId() == item.getId() &&
            booking.getStatuses().contains(APPROVED) && item.getAvailable().equals("true")) {
                feedback.setId(feedbackId++);
                feedback.setItem(item);
                feedback.setOwner(item.getOwner());
                feedback.setBooker(booker);
                feedback.setComment(feedbackDto.getComment());
                feedbackStorage.create(feedback.getId(), feedback);
            }
        }
        return feedbackMapper.createFeedbackDto(feedback);
    }

    public FeedbackDto getFeedbackById(Integer id) {
        Feedback feedback = feedbackStorage.getFeedback(id).orElseThrow(() ->
                new ObjectNotFoundException("Отзыва с номером = " + id + " не существует."));
        return feedbackMapper.createFeedbackDto(feedback);
    }

    public List<Feedback> getFeedbacksByItem(Integer itemId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        return feedbackStorage.findAll().stream()
                .filter(feedback -> Objects.equals(feedback.getItem().getId(), item.getId()))
                .collect(toList());
    }

    public List<Feedback> getFeedbacksByBooker(Integer bookerId) {
        User booker = userStorage.getUser(bookerId).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + bookerId + " не существует."));
        return feedbackStorage.findAll().stream()
                .filter(feedback -> Objects.equals(feedback.getBooker().getId(), booker.getId()))
                .collect(toList());
    }
}
