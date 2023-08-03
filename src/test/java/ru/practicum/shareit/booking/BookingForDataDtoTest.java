package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingForDataDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingForDataDtoTest {
    private final JacksonTester<BookingForDataDto> json;

    public BookingForDataDtoTest(@Autowired JacksonTester<BookingForDataDto> json) {
        this.json = json;
    }

    private final User booker = new User("ann", "ann@user.ru");

    @Test
    public void testJsonBookingForDataDto() throws Exception {
        booker.setId(2L);
        BookingForDataDto bookingForDataDto = new BookingForDataDto(
                1L, booker.getId(),
                LocalDateTime.of(2023, 10, 11, 12, 13, 14),
                LocalDateTime.of(2023, 11, 12, 13, 14, 15));
        JsonContent<BookingForDataDto> result = json.write(bookingForDataDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-11T12:13:14");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-11-12T13:14:15");
    }
}
