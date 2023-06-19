package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @ResponseBody
    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingService.addNewBooking(bookingDto, bookerId);
    }

    @ResponseBody
    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable int bookingId,
                             @RequestHeader("X-Sharer-User-Id") int userId, @RequestParam BookingStatus status) {
        return bookingService.update(bookingId, userId, status);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable int bookingId) {
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByBooker(@RequestHeader("X-Sharer-User-Id") int bookerId) {
        return bookingService.getBookingsByBooker(bookerId);
    }

    @GetMapping("/{itemId}")
    public List<BookingDto> getBookingByItem(@PathVariable int itemId) {
        return bookingService.getBookingByItem(itemId);
    }
}