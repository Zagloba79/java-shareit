package ru.practicum.shareit.feedback.dto;

import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.user.model.User;

public final class FeedbackMapper {
    private FeedbackMapper() {
    }

    public static FeedbackDto createFeedbackDto(Feedback feedback) {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setId(feedback.getId());
        feedbackDto.setItem(feedback.getItem());
        feedbackDto.setAuthor(feedback.getAuthor());
        feedbackDto.setComment(feedback.getComment());
        return feedbackDto;
    }

    public static Feedback createFeedback(FeedbackDto feedbackDto, User owner) {
        Feedback feedback = new Feedback();
        feedback.setItem(feedbackDto.getItem());
        feedback.setOwner(owner);
        feedback.setAuthor(feedbackDto.getAuthor());
        feedback.setComment(feedbackDto.getComment());
        return feedback;
    }
}