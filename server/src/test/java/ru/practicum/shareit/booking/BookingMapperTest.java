package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForDataDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.booking.model.BookingStatus.*;

public class BookingMapperTest {

    @Test
    public void createBookingDtoTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        Item item = new Item("name", "desc", true, owner, null);
        User booker = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        booker.setId(2L);
        Booking booking = new Booking(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                item,
                booker,
                WAITING);
        BookingDto bookingDto = BookingMapper.createBookingDto(booking);
        assertEquals(item, bookingDto.getItem());
        assertEquals(booker, bookingDto.getBooker());
        assertEquals(2L, bookingDto.getBooker().getId());
        assertEquals(WAITING, bookingDto.getStatus());
    }

    @Test
    public void createBookingForDatesDtoTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        Item item = new Item("name", "desc", true, owner, null);
        User booker = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        booker.setId(2L);
        Booking booking = new Booking(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                item,
                booker,
                APPROVED);
        BookingForDataDto bookingForDataDto = BookingMapper.createBookingForDatesDto(booking);
        assertNotNull(bookingForDataDto);
        assertEquals(2L, bookingForDataDto.getBookerId());
    }

    @Test
    public void createNewBookingTest() {
        User owner = UserMapper.createUser(new UserDto("alex", "alex@user.ru"));
        Item item = new Item("name", "desc", true, owner, null);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end =  LocalDateTime.now().plusDays(2);
        User booker = UserMapper.createUser(new UserDto("ann", "ann@user.ru"));
        Booking booking = BookingMapper.createNewBooking(start, end, item, booker);
        booking.setStatus(REJECTED);
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
        assertEquals(REJECTED, booking.getStatus());
    }
}