package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id", "name"})
public class User {
    private Integer id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
}
