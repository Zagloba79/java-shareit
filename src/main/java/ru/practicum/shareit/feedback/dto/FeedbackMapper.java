package ru.practicum.shareit.feedback.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FeedbackMapper {
    public static FeedbackDto createFeedbackDto(Feedback feedback) {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setId(feedback.getId());
        feedbackDto.setItemId(feedback.getItemId());
        feedbackDto.setAuthor(feedback.getAuthor());
        feedbackDto.setComment(feedback.getComment());
        feedbackDto.setCreate(feedback.getCreated());
        return feedbackDto;
    }

    public static Feedback createFeedback(FeedbackDto feedbackDto, User author) {
        Feedback feedback = new Feedback();
        feedback.setItemId(feedbackDto.getItemId());
        feedback.setAuthor(author);
        feedback.setComment(feedbackDto.getComment());
        feedback.setCreated(feedbackDto.getCreate());
        return feedback;
    }
}