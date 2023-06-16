package ru.practicum.shareit.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.dto.FeedbackMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
public class FeedbackStorage {
    private BookingService bookingService;
    private FeedbackMapper feedbackMapper;
    private Map<Integer, Feedback> feedbacks;

    public FeedbackDto create(int id, Feedback feedback) {
        feedbacks.put(id, feedback);
        return feedbackMapper.createFeedbackDto(feedbacks.get(id));
    }

    public Optional<Feedback> getFeedback(int id) {
        return Optional.of(feedbacks.get(id));
    }

    public FeedbackDto update(FeedbackDto feedbackDto, User owner) {
        Feedback f = feedbackMapper.createFeedback(feedbackDto, owner);
        feedbacks.put(f.getId(), f);
        return feedbackMapper.createFeedbackDto(feedbacks.get(f.getId()));
    }

    public void delete(int id) {
        feedbacks.remove(id);
    }

    public List<Feedback> findAll() {
        return new ArrayList<>(feedbacks.values());
    }
}