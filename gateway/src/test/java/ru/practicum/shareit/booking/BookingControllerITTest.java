package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.dto.BookingState.ALL;

@ExtendWith(MockitoExtension.class)
public class BookingControllerITTest {
    @Mock
    private BookingClient client;
    @InjectMocks
    private BookingController controller;
    private final BookItemRequestDto bookingDto = new BookItemRequestDto(1L,
            LocalDateTime.now().plusSeconds(1),
            LocalDateTime.now().plusSeconds(10));
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @Test
    public void createBookingTest() {
        long userId = 1L;
        when(client.bookItem(userId, bookingDto)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> objectResponseEntity1 = controller.create(userId, bookingDto);
        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    public void updateBookingTest() {
        Long userId = 1L;
        Long bookingId = 1L;
        boolean approved = true;
        when(client.approve(userId, bookingId, approved)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> objectResponseEntity1 = controller.approve(userId, bookingId, approved);
        assertEquals(objectResponseEntity, objectResponseEntity1);
    }

    @Test
    public void findBookingTest() {
        long userId = 1L;
        Long bookingId = 2L;
        when(client.getBooking(userId, bookingId)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> booking = controller.getBooking(userId, bookingId);
        assertEquals(objectResponseEntity, booking);
    }

    @Test
    public void findBookingsByBookerTest() {
        Long userId = 1L;
        when(client.getBookingsByOwner(userId, ALL, 0, 10)).thenReturn(objectResponseEntity);
        ResponseEntity<Object> bookings = controller.getBookingsByOwner(
                "all", userId, 0, 10);
        assertEquals(objectResponseEntity, bookings);
    }
}