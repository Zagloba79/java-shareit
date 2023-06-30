package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    private Integer id;
    @NotBlank
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
}