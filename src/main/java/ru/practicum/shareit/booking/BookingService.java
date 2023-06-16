package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.BookingStatus.*;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class BookingService {
    private BookingStorage bookingStorage;
    private int ids = 0;

    public void addNewBooking(LocalDateTime start, LocalDateTime end, Item item, User booker) {
        if (!item.isAvailable()) {
            log.info("This item has been rented");
        } else {
            Booking booking = new Booking();
            booking.setId(ids++);
            booking.setStart(start);
            booking.setEnd(end);
            booking.setItem(item);
            booking.setBooker(booker);
            booking.setStatus(WAITING);
            ArrayList<BookingStatus> statuses = new ArrayList<>();
            statuses.add(WAITING);
            booking.setStatuses(statuses);
            List<Booking> history = getBookingsByBooker(booker.getId());
            history.add(booking);
        }

    }

    public void approveBooking(Item item, User owner, User booker) {
        for (Booking booking : getBookingsByBooker(booker.getId())) {
            if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
                booking.setStatus(APPROVED);
                booking.getStatuses().add(APPROVED);
            }
        }
    }

    public void rejectBooking(Item item, User owner, User booker) {
        for (Booking booking : getBookingsByBooker(booker.getId())) {
            if (booking.getStatus().equals(WAITING) && item.getOwner().equals(owner)) {
                booking.setStatus(REJECTED);
                booking.getStatuses().add(REJECTED);
            }
        }
    }

    public void cancelBooking(Item item, User owner, User booker) {
        for (Booking booking : getBookingsByBooker(booker.getId())) {
            if ((booking.getStatus().equals(WAITING) || booking.getStatus().equals(APPROVED))
                    && item.getOwner().equals(owner)) {
                booking.setStatus(CANCELED);
                booking.getStatuses().add(CANCELED);
            }
        }
    }

    private Boolean validateBooking(Booking booking) {
        return booking.getStart().isBefore(LocalDateTime.now()) && booking.getEnd().isAfter(LocalDateTime.now())
                && booking.getStatus().equals(APPROVED);
    }

    public List<Booking> getBookingsByItem(Item item) {
        int id = item.getId();
        return bookingStorage.findAll().stream()
                .filter(booking -> booking.getItem().getId() == id)
                .collect(toList());
    }

    public List<Booking> getBookingsByBooker(int bookerId) {
        return bookingStorage.findAll().stream()
                .filter(booking -> booking.getBooker().getId() == bookerId)
                .collect(toList());
    }
}