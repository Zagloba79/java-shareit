package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody NewBookingDto bookingDto,
                                    @RequestHeader(Constants.USER_ID) Long bookerId) {
        return bookingService.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@PathVariable Long bookingId,
                                    @RequestHeader(Constants.USER_ID) Long userId,
                                    @RequestParam Boolean approved) {
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader(Constants.USER_ID) Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUserAndState(
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestHeader(Constants.USER_ID) Long userId) {
        return bookingService.getBookingsByBookerAndState(from, size, state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerAndState(
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestHeader(Constants.USER_ID) Long userId) {
        return bookingService.getBookingsByOwnerAndState(from, size, state, userId);
    }
}