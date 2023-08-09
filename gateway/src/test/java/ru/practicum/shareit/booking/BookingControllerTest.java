package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_ID;
import static ru.practicum.shareit.booking.dto.BookingState.ALL;

@AutoConfigureMockMvc
@SpringBootTest
class BookingControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingClient bookingClient;
    @MockBean
    private UserClient userClient;
    private final UserDto userDto = new UserDto(1L, "name", "desc");
    private Long userId = 1L;
    private final BookItemRequestDto bookingDto = new BookItemRequestDto(1L,
            LocalDateTime.of(2023, 12, 1, 1, 1, 1),
            LocalDateTime.of(2023, 12, 1, 1, 1, 2));
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);
    private final ResponseEntity<Object> badRequestEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    @Test
    public void createBookingTest() throws Exception {
        when(bookingClient.bookItem(userDto.getId(), bookingDto)).thenReturn(objectResponseEntity);
        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, userDto.getId()).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    public void createBookingNotValidTest() throws Exception {
        when(bookingClient.bookItem(anyLong(), any())).thenReturn(badRequestEntity);
        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, -1).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, -1L).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
        bookingDto.setStart(LocalDateTime.now().plusSeconds(1));
        bookingDto.setEnd(LocalDateTime.now().minusDays(1));
        mvc.perform(post("/bookings", bookingDto)
                        .contentType("application/json")
                        .header(USER_ID, -1L).content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
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
    public void updateBookingBadRequestTest() throws Exception {
        Long userId = 1L;
        Long bookingId = -1L;
        when(bookingClient.update(userId, bookingId, true)).thenReturn(badRequestEntity);
        mvc.perform(patch("/bookings/{bookingId}", -1)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .param("approved", String.valueOf(true))
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getBookingTest() throws Exception {
        long userId = 1L;
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
    public void getBookingNotEntityTest() throws Exception {
        long userId = 1L;
        Long bookingId = -1L;
        when(bookingClient.getBooking(userId, bookingId)).thenReturn(badRequestEntity);
        mvc.perform(get("/bookings/{bookingId}", -1)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isNotFound());
    }

    @Test
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
    public void getBookingsByOwnerNotValidUserTest() throws Exception {
        when(bookingClient.getBookingsByOwner(-1L, ALL, 0, 10))
                .thenReturn(badRequestEntity);
        mvc.perform(get("/bookings/owner")
                        .header(USER_ID, -1L)
                        .param("state", String.valueOf(ALL))
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());
    }
}