package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingForDataDto {
    private Long id;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
