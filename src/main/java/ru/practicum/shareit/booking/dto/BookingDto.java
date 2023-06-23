package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = { "id", "statuses" })
public class BookingDto {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
    private User booker;
    private BookingStatus status;
    private ArrayList<BookingStatus> statuses;
}
