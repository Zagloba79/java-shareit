package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.handler.ErrorResponse;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_ID;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    ItemRequestService itemRequestService;
    @InjectMocks
    ItemRequestController itemRequestController;

    protected ObjectMapper objectMapper = new ObjectMapper();
    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
    }

    @Test
    public void test_positive_create_request() throws Exception {
        ItemRequestDto request = new ItemRequestDto();
        Mockito
                .when(itemRequestService.create(Mockito.isA(ItemRequestDto.class), Mockito.anyLong()))
                .thenThrow(new ObjectNotFoundException("!!!!"));
        String requestAsString = objectMapper.writeValueAsString(request);
        String responseAsString = mockMvc.perform(post("/requests")
                        .header(USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsString))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ErrorResponse apiErrorResponse = objectMapper.readValue(responseAsString, ErrorResponse.class);
        assertEquals("Запрос без описания", apiErrorResponse.getError());
    }
}
