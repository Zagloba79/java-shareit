package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.Constants.USER_ID;

import ru.practicum.shareit.item.dto.ItemForAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService service;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto = new UserDto("Alex", "alexandr@user.ru");
    private final User user = new User("Alex", "alexandr@user.ru");
    LocalDateTime created = LocalDateTime.of(2023, 7, 28, 0, 0, 0);
    private final ItemRequestDto requestDto = new ItemRequestDto("requestDescription", created);
    private final RequestWithItemsDto requestWithItems = new RequestWithItemsDto(
            "requestDescription", created, new ArrayList<ItemForAnswerDto>());
    private final List<RequestWithItemsDto> requestsWithItems = new ArrayList<>();


    @Test
    void createRequest() throws Exception {
        userDto.setId(1L);
        requestDto.setRequester(user);
        when(service.create(any(), anyLong()))
                .thenReturn(requestDto);
        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.requester.id", is(requestDto.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$.requester.name", is(requestDto.getRequester().getName())))
                .andExpect(jsonPath("$.requester.email", is(requestDto.getRequester().getEmail())))
                .andExpect(jsonPath("$.created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getRequestById() throws Exception {
        when(service.getRequestById(anyLong(), anyLong()))
                .thenReturn(requestWithItems);
        mvc.perform(get("/requests/1")
                        .content(mapper.writeValueAsString(requestWithItems))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestWithItems.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestWithItems.getDescription())))
                .andExpect(jsonPath("$.created",
                        is(requestWithItems.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.items", is(requestWithItems.getItems())));
    }

    @Test
    void getRequestsByRequester() throws Exception {
        when(service.getRequestsByRequester(anyLong()))
                .thenReturn(List.of(requestWithItems));
        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(requestsWithItems))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestWithItems.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestWithItems.getDescription())))
                .andExpect(jsonPath("$.[0].created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].items", is(requestWithItems.getItems())));
    }

    @Test
    void getRequestsPageable() throws Exception {
        when(service.getRequestsPageable(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestWithItems));
        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(requestsWithItems))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(requestWithItems.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestWithItems.getDescription())))
                .andExpect(jsonPath("$.[0].created",
                        is(requestDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].items", is(requestWithItems.getItems())));
    }
}