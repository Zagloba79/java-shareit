package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.Constants.USER_ID;

@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemClient itemClient;
    private final Long itemId = 1L;
    private final Long userId = 1L;
    private final ResponseEntity<Object> wellRequest = new ResponseEntity<>(HttpStatus.OK);
    private final ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 2L);

    @Test
    public void createItemTest() throws Exception {
        when(itemClient.create(userId, itemDto)).thenReturn(wellRequest);
        mockMvc.perform(post("/items", itemDto)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionCreateNotValidItemTest() throws Exception {
        itemDto.setDescription(null);
        when(itemClient.create(userId, itemDto)).thenReturn(wellRequest);
        mockMvc.perform(post("/items", itemDto)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void getItemTest() throws Exception {
        when(itemClient.getItemById(userId, itemId)).thenReturn(wellRequest);
        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(USER_ID, userId))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionGetItemNotValidIdTest() throws Exception {
        when(itemClient.getItemById(userId, -1L)).thenReturn(wellRequest);
        mockMvc.perform(get("/items/{itemId}", -1)
                        .header(USER_ID, userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateItemTest() throws Exception {
        ItemDto itemDto1 = itemDto;
        itemDto1.setDescription("изменено");
        when(itemClient.update(itemDto1, itemId, userId)).thenReturn(wellRequest);
        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto1)))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionUpdateItemNotValidTest() throws Exception {
        ItemDto itemDto1 = itemDto;
        itemDto1.setDescription("");
        when(itemClient.update(any(), anyLong(), anyLong())).thenReturn(wellRequest);
        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDto1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllItemByUserTest() throws Exception {
        when(itemClient.getItemsByOwner(userId, 0, 10)).thenReturn(wellRequest);
        mockMvc.perform(get("/items")
                        .header(USER_ID, userId)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionGetAllItemByUserNotValidUserTest() throws Exception {
        when(itemClient.getItemsByOwner(-1L, 0, 10)).thenReturn(wellRequest);
        mockMvc.perform(get("/items")
                        .header(USER_ID, -1)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getItemsByQueryTest() throws Exception {
        when(itemClient.getItemsByQuery(userId, "asd", 0, 10)).thenReturn(wellRequest);
        mockMvc.perform(get("/items/search")
                        .header(USER_ID, userId)
                        .param("text", "asd")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk());
    }

    @Test
    public void exceptionGetItemsByQueryNotValidUserTest() throws Exception {
        when(itemClient.getItemsByQuery(-1L, "asd", 0, 10)).thenReturn(wellRequest);
        mockMvc.perform(get("/items/search")
                        .header(USER_ID, -1)
                        .param("text", "asd")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(1L, "tet", "name",
                LocalDateTime.of(2023, 1, 2, 3, 4, 5));
        when(itemClient.createComment(commentDto, itemId, userId)).thenReturn(wellRequest);
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void createCommentNotValidCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto();
        when(itemClient.createComment(commentDto, itemId, userId)).thenReturn(wellRequest);
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());
    }
}