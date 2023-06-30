package ru.practicum.shareit.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.dto.FeedbackMapper;
import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FeedbackStorage {

    private Map<Integer, Feedback> feedbacks;
    private Integer feedbackId = 1;

    public Feedback create(Feedback feedback) {
        feedback.setId(feedbackId++);
        feedbacks.put(feedback.getId(), feedback);
        return feedbacks.get(feedback.getId());
    }

    public Optional<Feedback> getFeedback(int id) {
        return Optional.of(feedbacks.get(id));
    }

    public FeedbackDto update(FeedbackDto feedbackDto, User owner) {
        Feedback f = FeedbackMapper.createFeedback(feedbackDto, owner);
        feedbacks.put(f.getId(), f);
        return FeedbackMapper.createFeedbackDto(feedbacks.get(f.getId()));
    }

    public void delete(int id) {
        feedbacks.remove(id);
    }

    public List<Feedback> findAll() {
        return new ArrayList<>(feedbacks.values());
    }
}