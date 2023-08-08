package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_ID;
import static ru.practicum.shareit.booking.dto.BookingState.ALL;

@WebMvcTest(BookingController.class)
class BookingControllerITTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingClient bookingClient;
    private final BookItemRequestDto bookingDto = new BookItemRequestDto(1L,
            LocalDateTime.now().plusSeconds(1),
            LocalDateTime.now().plusSeconds(10));
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @Test
    @DirtiesContext
    public void createBookingTest() throws Exception {
        Long userId = 1L;
        when(bookingClient.create(userId, bookingDto)).thenReturn(objectResponseEntity);
        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, userId).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

    }

    @Test
    @DirtiesContext
    public void createBookingNotValidTest() throws Exception {
        Long userId = -1L;
        when(bookingClient.create(userId, bookingDto)).thenReturn(objectResponseEntity);
        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, -1L).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, userId).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));
        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, userId).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    public void updateBookingTest() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        when(bookingClient.update(bookingId, userId, true)).thenReturn(objectResponseEntity);
        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .param("approved", String.valueOf(true))
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    public void updateBookingBadRequestTest() throws Exception {
        Long userId = 1L;
        Long bookingId = -1L;
        when(bookingClient.update(userId, bookingId, true)).thenReturn(objectResponseEntity);
        mvc.perform(patch("/bookings/{bookingId}", -1L)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .param("approved", String.valueOf(true))
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    public void getBookingTest() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        when(bookingClient.getBooking(userId, bookingId)).thenReturn(objectResponseEntity);
        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    public void getBookingNotEntityTest() throws Exception {
        Long userId = 1L;
        Long bookingId = -1L;
        when(bookingClient.getBooking(userId, bookingId)).thenReturn(objectResponseEntity);
        mvc.perform(get("/bookings/{bookingId}", -1L)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    public void getBookingsByOwnerTest() throws Exception {
        Long userId = 1L;
        when(bookingClient.getBookingsByOwner(userId, ALL, 0, 10))
                .thenReturn(objectResponseEntity);
        mvc.perform(get("/bookings")
                        .header(USER_ID, userId)
                        .param("state", String.valueOf(ALL))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    public void getBookingsByOwnerNotValidUserTest() throws Exception {
        when(bookingClient.getBookingsByOwner(-1L, ALL, 0, 10))
                .thenReturn(objectResponseEntity);
        mvc.perform(get("/bookings")
                        .header(USER_ID, -1L)
                        .param("state", String.valueOf(ALL))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());
    }
}