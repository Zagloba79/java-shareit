package ru.practicum.shareit.itemRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_ID;

@AutoConfigureMockMvc
@SpringBootTest
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    private final Long userId = 1L;

    private final ResponseEntity<Object> wellRequest = new ResponseEntity<>(HttpStatus.OK);
    private final UserDto userDto = new UserDto(1L, "Asd", "hju@ert.tu");
    private final ItemRequestDto itemDto = new ItemRequestDto(5L, "iuy", userDto,
            LocalDateTime.of(2023, 1, 2, 3, 4, 5));

    @Test
    public void createRequestTest() throws Exception {
        when(itemRequestClient.create(itemDto, userId)).thenReturn(wellRequest);
        mockMvc.perform(post("/requests", itemDto)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionCreateRequestNotValidTest() throws Exception {
        itemDto.setDescription("");
        when(itemRequestClient.create(itemDto, userId)).thenReturn(wellRequest);
        mockMvc.perform(post("/requests", itemDto)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getRequestsByUserTest() throws Exception {
        when(itemRequestClient.getOwnItemRequests(userId)).thenReturn(wellRequest);
        mockMvc.perform(get("/requests")
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    public void getRequestsTest() throws Exception {
        when(itemRequestClient.getAllItemRequests(userId, 0, 10)).thenReturn(wellRequest);
        mockMvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionGetAllItemRequestsByNotValidUserTest() throws Exception {
        when(itemRequestClient.getAllItemRequests(-1L, 0, 10)).thenReturn(wellRequest);
        mockMvc.perform(get("/requests/all")
                        .contentType("application/json")
                        .header(USER_ID, -1)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getItemRequestByIdTest() throws Exception {
        when(itemRequestClient.getItemRequestById(userId, 1L)).thenReturn(wellRequest);
        mockMvc.perform(get("/requests/{requestId}", 1)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionGetItemRequestByIdNotValidRequestTest() throws Exception {
        when(itemRequestClient.getItemRequestById(userId, -1L)).thenReturn(wellRequest);
        mockMvc.perform(get("/requests/{requestId}", -1)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isBadRequest());
    }
}