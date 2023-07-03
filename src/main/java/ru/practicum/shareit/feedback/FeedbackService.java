package ru.practicum.shareit.feedback;

import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.model.Feedback;

import java.util.List;

public interface FeedbackService {

    FeedbackDto createFeedback(FeedbackDto feedbackDto, Integer authorId, Integer itemId);

    FeedbackDto getFeedbackById(Integer id);

    List<Feedback> getFeedbacksByItem(Integer itemId);

    List<Feedback> getFeedbacksByAuthor(Integer authorId);
}
