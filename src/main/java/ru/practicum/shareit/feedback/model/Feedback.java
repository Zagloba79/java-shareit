package ru.practicum.shareit.feedback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    private Integer id;
    private Item item;
    private User owner;
    private User author;
    private String comment;
}
