package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;


@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class Item {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    ItemRequest request;

    public Item(String name, String description, Boolean available, User owner, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}