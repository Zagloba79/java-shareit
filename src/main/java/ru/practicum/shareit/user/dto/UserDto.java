package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}