package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;

    public ItemRequestDtoTest(@Autowired JacksonTester<ItemRequestDto> json) {
        this.json = json;
    }

    private final User requester = new User("ann", "ann@user.ru");

    @Test
    public void testJsonItemRequestDto() throws Exception {
        requester.setId(1L);
        ItemRequestDto itemRequestDto = new ItemRequestDto(
                "desc",
                LocalDateTime.of(2023, 1, 2, 3, 4, 5));
        itemRequestDto.setId(2L);
        itemRequestDto.setRequester(requester);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-01-02T03:04:05");
        assertThat(result).extractingJsonPathStringValue("$.requester.name").isEqualTo("ann");
    }
}
