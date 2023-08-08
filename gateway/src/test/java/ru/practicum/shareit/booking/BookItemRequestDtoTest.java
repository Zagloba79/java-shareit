package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookItemRequestDtoTest {
    private final JacksonTester<BookItemRequestDto> json;

    public BookItemRequestDtoTest(@Autowired JacksonTester<BookItemRequestDto> json) {
        this.json = json;
    }

    @Test
    public void testJsonBookingDto() throws Exception {
        BookItemRequestDto bookingDto = new BookItemRequestDto(
                1L,
                LocalDateTime.of(2023, 10, 11, 12, 13, 14),
                LocalDateTime.of(2023, 11, 12, 13, 14, 15));
        JsonContent<BookItemRequestDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-11T12:13:14");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-11-12T13:14:15");
    }
}
