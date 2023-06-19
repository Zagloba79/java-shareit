package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@AllArgsConstructor
public class Item {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private List<String> requests;

    public Item(String name, String description, boolean available, User owner, List<String> requests) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requests = requests;
    }
}