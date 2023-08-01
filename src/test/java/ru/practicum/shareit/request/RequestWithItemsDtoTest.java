package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;
import ru.practicum.shareit.request.dto.RequestWithItemsDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestWithItemsDtoTest {
    private final JacksonTester<RequestWithItemsDto> json;

    public RequestWithItemsDtoTest(@Autowired JacksonTester<RequestWithItemsDto> json) {
        this.json = json;
    }

    private final User requester = new User("ann", "ann@user.ru");

    @Test
    public void testJsonRequestWithItemsDto() throws Exception {
        ItemForAnswerDto item = new ItemForAnswerDto("name", "desc", 5L, true);
        item.setId(1L);
        requester.setId(2L);
        RequestWithItemsDto requestWithItemsDto = new RequestWithItemsDto(
                "Description",
                LocalDateTime.of(2023, 10, 11, 12, 13, 14),
                List.of(item));
        requestWithItemsDto.setId(5L);
        JsonContent<RequestWithItemsDto> result = json.write(requestWithItemsDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-10-11T12:13:14");
    }
}