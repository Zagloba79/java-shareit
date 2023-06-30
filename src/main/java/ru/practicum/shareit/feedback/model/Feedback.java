package ru.practicum.shareit.feedback.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class Feedback {
    private Integer id;
    @NotBlank
    private Item item;
    private User owner;
    private User author;
    @NotBlank
    private String comment;
}