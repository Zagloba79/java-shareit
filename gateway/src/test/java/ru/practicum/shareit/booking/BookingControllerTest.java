package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.dto.BookingState.ALL;

public class BookingControllerTest {
    @Mock
    private BookingClient bookingClient;
    @InjectMocks
    private BookingController bookingController;
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @Test
    public void createBookingTest() {
        Long userId = 1L;
        BookItemRequestDto bookingDto = new BookItemRequestDto(1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(10));
        when(bookingClient.create(userId, bookingDto)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> objectResponseEntity1 = bookingController.create(userId, bookingDto);
        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    public void updateBookingTest() {
        Long userId = 1L;
        Long bookingId = 1L;
        boolean approved = true;
        when(bookingClient.update(userId, bookingId, approved)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> objectResponseEntity1 = bookingController.update(userId, bookingId, approved);
        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    public void findBookingTest() {
        Long userId = 1L;
        Long bookingId = 2L;
        when(bookingClient.getBooking(userId, bookingId)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> booking = bookingController.getBooking(userId, bookingId);
        assertEquals(objectResponseEntity, booking);
    }

    @Test
    public void findBookingsByBookerTest() {
        Long userId = 1L;
        when(bookingClient.getBookingsByOwner(userId, ALL, 0, 10)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> bookings = bookingController.getBookingsByOwner(
                "all", userId, 0, 10);
        assertEquals(objectResponseEntity, bookings);
    }
}
