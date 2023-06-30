package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestDto {
    Integer id;
    @NotBlank
    String description;
    User requester;
    @NotBlank
    LocalDate created;
}