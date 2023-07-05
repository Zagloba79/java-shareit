package ru.practicum.shareit.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.dto.FeedbackMapper;
import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackStorage feedbackStorage;
    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public FeedbackDto createFeedback(FeedbackDto feedbackDto, Long authorId, Long itemId) {
        Feedback feedback = new Feedback();
        User author = userStorage.getUser(authorId);
        Item item = itemStorage.getItem(itemId);
        for (Booking booking : bookingStorage.getBookingsByBooker(authorId)) {
            if (booking.getItem().getId().equals(item.getId()) && item.getAvailable().equals(true)) {
                feedback.setItemId(itemId);
                feedback.setAuthor(author);
                feedback.setComment(feedbackDto.getComment());
                feedbackStorage.create(feedback);
            }
        }
        return FeedbackMapper.createFeedbackDto(feedback);
    }

    @Override
    public FeedbackDto getFeedbackById(Long id) {
        Feedback feedback = feedbackStorage.getFeedback(id);
        return FeedbackMapper.createFeedbackDto(feedback);
    }

    @Override
    public List<Feedback> getFeedbacksByItem(Long itemId) {
        Item item = itemStorage.getItem(itemId);
        return feedbackStorage.findAll().stream().filter(
                feedback -> feedback.getItemId().equals(item.getId()))
                .collect(toList());
    }

    @Override
    public List<Feedback> getFeedbacksByAuthor(Long authorId) {
        User author = userStorage.getUser(authorId);
        return feedbackStorage.findAll().stream().filter(
                feedback -> feedback.getAuthor().getId().equals(author.getId()))
                .collect(toList());
    }
}