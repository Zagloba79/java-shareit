package ru.practicum.shareit.feedback.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.user.model.User;

@Component
public class FeedbackMapper {
    public FeedbackDto createFeedbackDto(Feedback feedback) {
        return new FeedbackDto(
                feedback.getId(),
                feedback.getItem(),
                feedback.getBooker(),
                feedback.getComment());
    }

    public Feedback createFeedback(FeedbackDto feedbackDto, User owner) {
        return new Feedback(
                feedbackDto.getId(),
                feedbackDto.getItem(),
                owner,
                feedbackDto.getBooker(),
                feedbackDto.getComment()
        );
    }
}
