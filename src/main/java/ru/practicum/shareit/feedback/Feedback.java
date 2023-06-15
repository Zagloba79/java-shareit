package ru.practicum.shareit.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
public class Feedback {
    private int id;
    private Item item;
    private User owner;
    private User booker;
    private String comment;
}
