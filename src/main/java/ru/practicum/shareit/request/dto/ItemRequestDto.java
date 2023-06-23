package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class ItemRequestDto {
    Integer id;
    Item item;
    User requester;
}