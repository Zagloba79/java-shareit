package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestBody NewBookingDto bookingDto,
                             @RequestHeader(USER_ID) Long bookerId) {
        return bookingService.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestHeader(USER_ID) Long userId, @RequestParam Boolean approved) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader(USER_ID) Long userId) {
        return bookingService.getBookingDtoById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByUserAndState(
            @RequestParam(name = "state", defaultValue = "ALL") String state,
             @RequestHeader(USER_ID) Long userId) {
        return bookingService.getBookingsDtoByUserAndState(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerAndState(
            @RequestParam(name = "state", defaultValue = "ALL") String state,
             @RequestHeader(USER_ID) Long userId) {
        return bookingService.getBookingsDtoByOwnerAndState(state, userId);
    }
}