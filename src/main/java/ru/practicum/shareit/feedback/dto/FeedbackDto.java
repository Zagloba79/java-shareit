package ru.practicum.shareit.feedback.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackDto {
    private Long id;
    private Long itemId;
    private User author;
    @NotBlank
    private String comment;
    private LocalDateTime create;
}