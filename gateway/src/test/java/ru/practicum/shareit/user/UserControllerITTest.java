package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerITTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserClient userClient;
    private final ResponseEntity<Object> objectResponseEntity = new ResponseEntity<>(HttpStatus.OK);

    private final UserDto userDto = new UserDto(1L, "hgf", "kij@er.ty");

    @Test
    public void createUserTest() throws Exception {
        when(userClient.create(userDto)).thenReturn(objectResponseEntity);
        mockMvc.perform(post("/users", userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

    }

    @Test
    public void createUserNotValidTest() throws Exception {
        userDto.setEmail("");
        when(userClient.create(userDto)).thenReturn(objectResponseEntity);
        mockMvc.perform(post("/users", userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void updateUserTest() throws Exception {
        UserDto user1 = new UserDto(1L, "dsa", "kij@er.ty");
        when(userClient.update(user1, userDto.getId())).thenReturn(objectResponseEntity);
        mockMvc.perform(post("/users", userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserNotValidTest() throws Exception {
        UserDto user1 = new UserDto(1L, "dsa", "");
        when(userClient.update(user1, userDto.getId())).thenReturn(objectResponseEntity);
        mockMvc.perform(post("/users", userDto)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllUsersTest() throws Exception {
        when(userClient.getUsers()).thenReturn(objectResponseEntity);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserTest() throws Exception {
        when(userClient.getUserById(userDto.getId())).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/users/{userId}", userDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserNotValidIdTest() throws Exception {
        when(userClient.getUserById(-1L)).thenReturn(objectResponseEntity);

        mockMvc.perform(get("/users/{userId}", -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteUserTest() throws Exception {
        when(userClient.delete(userDto.getId())).thenReturn(objectResponseEntity);

        mockMvc.perform(delete("/users/{userId}", userDto.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserNotValidIdTest() throws Exception {
        when(userClient.delete(-1L)).thenReturn(objectResponseEntity);

        mockMvc.perform(delete("/users/{userId}", -1))
                .andExpect(status().isBadRequest());
    }
}