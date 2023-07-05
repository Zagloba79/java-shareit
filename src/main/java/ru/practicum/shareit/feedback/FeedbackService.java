package ru.practicum.shareit.feedback;

import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.model.Feedback;

import java.util.List;

public interface FeedbackService {

    FeedbackDto createFeedback(FeedbackDto feedbackDto, Long authorId, Long itemId);

    FeedbackDto getFeedbackById(Long id);

    List<Feedback> getFeedbacksByItem(Long itemId);

    List<Feedback> getFeedbacksByAuthor(Long authorId);
}
