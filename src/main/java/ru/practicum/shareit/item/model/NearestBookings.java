package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NearestBookings {
    @NotNull
    private LocalDateTime previousBookingStart;
    @NotNull
    private LocalDateTime previousBookingEnd;
    @NotNull
    private LocalDateTime nextBookingStart;
    @NotNull
    private LocalDateTime nextBookingEnd;
}
