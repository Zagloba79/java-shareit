package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequest {
    private Integer id;
    private String description;
    private User requester;
    private LocalDate created;
}