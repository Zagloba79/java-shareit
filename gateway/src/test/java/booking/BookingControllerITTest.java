package booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
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

    private final long userId = 1L;

    private final long itemId = 1L;

    private final long bookingId = 1L;

    private final BookItemRequestDto bookingDto = new BookItemRequestDto(1L,
            LocalDateTime.now().plusSeconds(1),
            LocalDateTime.now().plusSeconds(10));

    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    @Test
    public void postBookingTest() throws Exception {

        when(bookingClient.create(userId, bookingDto)).thenReturn(objectResponseEntity);

        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, userId).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

    }

    @Test
    public void postBookingNotValidTest() throws Exception {

        when(bookingClient.create(userId, bookingDto)).thenReturn(objectResponseEntity);

        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, -1).content(objectMapper.writeValueAsString(bookingDto))
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
    public void approvedBookingTest() throws Exception {

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
    public void approvedBookingBadRequestTest() throws Exception {

        when(bookingClient.update(userId, bookingId, true)).thenReturn(objectResponseEntity);

        mvc.perform(patch("/bookings/{bookingId}", -1)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .param("approved", String.valueOf(true))
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findBooking() throws Exception {
        when(bookingClient.getBooking(userId, bookingId)).thenReturn(objectResponseEntity);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    public void findBookingNotEntity() throws Exception {
        when(bookingClient.getBooking(userId, bookingId)).thenReturn(objectResponseEntity);

        mvc.perform(get("/bookings/{bookingId}", -1)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findListBooking() throws Exception {
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
    public void findListBookingNotValidUser() throws Exception {
        when(bookingClient.getBookingsByOwner(-1L, ALL, 0, 10))
                .thenReturn(objectResponseEntity);

        mvc.perform(get("/bookings")
                        .header(USER_ID, -1)
                        .param("state", String.valueOf(ALL))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());
    }
}