package ru.practicum.shareit.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Data
@AllArgsConstructor
public class Feedback {
    private Item item;
    private User owner;
    private User booker;
    private String comment;
}
