package ru.practicum.shareit.feedback.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class FeedbackDto {
    private Integer id;
    @NotBlank
    private Item item;
    private User author;
    @NotBlank
    private String comment;
}