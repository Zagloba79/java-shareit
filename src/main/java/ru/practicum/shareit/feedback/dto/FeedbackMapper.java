package ru.practicum.shareit.feedback.dto;

import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.user.model.User;

public final class FeedbackMapper {
    private FeedbackMapper() {
    }

    public static FeedbackDto createFeedbackDto(Feedback feedback) {
        return new FeedbackDto(
                feedback.getId(),
                feedback.getItem(),
                feedback.getAuthor(),
                feedback.getComment());
    }

    public static Feedback createFeedback(FeedbackDto feedbackDto, User owner) {
        return new Feedback(
                feedbackDto.getId(),
                feedbackDto.getItem(),
                owner,
                feedbackDto.getAuthor(),
                feedbackDto.getComment()
        );
    }
}
