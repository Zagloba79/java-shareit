package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @ResponseBody
    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto,
                             @RequestHeader(USER_ID) Integer bookerId) {
        return bookingService.addNewBooking(bookingDto, bookerId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Integer bookingId,
                             @RequestHeader(USER_ID) Integer userId, @RequestParam BookingStatus status) {
        return bookingService.update(bookingId, userId, status);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Integer bookingId,
                                     @RequestHeader(USER_ID) Integer userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader(USER_ID) Integer userId) {
        return bookingService.getAllBookings(userId);
    }

    @GetMapping("/{bookerId}")
    public List<BookingDto> getBookingsByBooker(@RequestHeader(USER_ID) Integer bookerId) {
        return bookingService.getBookingsByBooker(bookerId);
    }

    @GetMapping("/{itemId}")
    public List<BookingDto> getBookingByItem(@PathVariable Integer itemId,
                                             @RequestHeader(USER_ID) Integer bookerId) {
        return bookingService.getBookingByItem(itemId, bookerId);
    }
}