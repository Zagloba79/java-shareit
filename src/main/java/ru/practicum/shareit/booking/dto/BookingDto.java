package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingDto {
    private Integer id;
    @NotBlank
    private LocalDateTime start;
    @NotBlank
    private LocalDateTime end;
    @NotBlank
    private Item item;
    @NotBlank
    private User booker;
    @NotBlank
    private BookingStatus status;
}