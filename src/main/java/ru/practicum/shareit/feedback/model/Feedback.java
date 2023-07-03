package ru.practicum.shareit.feedback.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Feedback {
    private Integer id;
    private Integer itemId;
    private User author;
    private String comment;
    private LocalDateTime create;
}