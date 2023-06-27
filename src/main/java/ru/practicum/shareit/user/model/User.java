package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@EqualsAndHashCode//(exclude = {"id", "name"})
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String email;
}
