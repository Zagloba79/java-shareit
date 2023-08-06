package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
    private User requester;
    @NotNull
    private LocalDateTime created;

    public ItemRequestDto(String description, LocalDateTime created) {
        this.description = description;
        this.created = created;
    }
}