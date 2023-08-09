package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.BookingStatus.*;


@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    BookingServiceImpl service;
    @Mock
    BookingRepository repository;
    @Mock
    EntityHandler handler;
    final LocalDateTime start = LocalDateTime.now().plusSeconds(1);
    final LocalDateTime end = LocalDateTime.now().plusDays(2);
    private final User owner = new User("iAm", "iam@user.ru");
    private final User booker = new User("booker", "booker@user.ru");
    private final Item item = new Item("name", "Desc", true, owner, null);
    private final ItemDto itemDto = new ItemDto("name", "Desc", true, null);
    private final NewBookingDto newBookingDto = new NewBookingDto(
            start, end, itemDto.getId());
    private final Booking booking = new Booking(start, end, item, booker, WAITING);



    @Test
    public void createBookingTest() {
        owner.setId(12L);
        doNothing().when(handler).bookingValidate(any(), any());
        when(handler.getItemFromOpt(item.getId())).thenReturn(item);
        doNothing().when(handler).itemIsAvailable(item);
        when(handler.getUserFromOpt(booker.getId())).thenReturn(booker);
        when(repository.save(any())).thenReturn(booking);
        BookingDto bookingFromDb = service.create(newBookingDto, booker.getId());
        assertEquals(item, bookingFromDb.getItem());
        assertEquals(booker, bookingFromDb.getBooker());
        assertEquals(WAITING, bookingFromDb.getStatus());
    }

    @Test
    public void approvedBookingByOwnerTest() {
        owner.setId(1L);
        booker.setId(2L);
        booking.setId(3L);
        when(handler.getUserFromOpt(anyLong())).thenReturn(owner);
        when(handler.getBookingByIdAndOwnerIdFromOpt(booking.getId(), owner.getId())).thenReturn(booking);
        when(repository.save(any())).thenReturn(booking);
        BookingDto updatedBooking = service.update(booking.getId(), owner.getId(), true);
        assertEquals(item, updatedBooking.getItem());
        assertEquals(booker, updatedBooking.getBooker());
        assertEquals(APPROVED, updatedBooking.getStatus());
    }

    @Test
    public void rejectBookingByOwnerTest() {
        owner.setId(1L);
        booker.setId(2L);
        booking.setId(3L);
        when(handler.getUserFromOpt(anyLong())).thenReturn(owner);
        when(handler.getBookingByIdAndOwnerIdFromOpt(booking.getId(), owner.getId())).thenReturn(booking);
        when(repository.save(any())).thenReturn(booking);
        BookingDto updatedBooking = service.update(booking.getId(), owner.getId(), false);
        assertEquals(item, updatedBooking.getItem());
        assertEquals(booker, updatedBooking.getBooker());
        assertEquals(REJECTED, updatedBooking.getStatus());
    }

    @Test
    public void getBookingsByBookerAndStateTest() {
        booker.setId(1L);
        int from = 0;
        int size = 10;
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2),
                item, booker, WAITING);
        Booking booking2 = new Booking(
                LocalDateTime.now().plusSeconds(10), LocalDateTime.now().plusSeconds(12),
                item, booker, WAITING);
        Booking booking3 = new Booking(
                LocalDateTime.now().plusSeconds(20), LocalDateTime.now().plusSeconds(22),
                item, booker, WAITING);
        bookings.add(booking1);
        bookings.add(booking2);
        bookings.add(booking3);
        when(handler.getUserFromOpt(anyLong())).thenReturn(booker);
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size,
                Sort.by("start").descending());
        when(repository.findByBookerId(booker.getId(), pageable))
                .thenReturn(bookings);
        List<BookingDto> bookingsFromDb = service.getBookingsByBookerAndState(from, size,
        "ALL", booker.getId());
        assertEquals(3, bookingsFromDb.size());
    }

    @Test
    public void getBookingsByOwnerAndStateTest() {
        owner.setId(1L);
        booker.setId(2L);
        int from = 0;
        int size = 10;
        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking(
                LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2),
                item, booker, WAITING);
        Booking booking2 = new Booking(
                LocalDateTime.now().plusSeconds(10), LocalDateTime.now().plusSeconds(12),
                item, booker, WAITING);
        Booking booking3 = new Booking(
                LocalDateTime.now().plusSeconds(20), LocalDateTime.now().plusSeconds(22),
                item, booker, WAITING);
        bookings.add(booking1);
        bookings.add(booking2);
        bookings.add(booking3);
        when(handler.getUserFromOpt(anyLong())).thenReturn(owner);
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size,
                Sort.by("start").descending());
        when(repository.findByItem_OwnerId(owner.getId(), pageable))
                .thenReturn(bookings);
        List<BookingDto> bookingsFromDb = service.getBookingsByOwnerAndState(from, size,
                "ALL", owner.getId());
        assertEquals(3, bookingsFromDb.size());
    }
}

