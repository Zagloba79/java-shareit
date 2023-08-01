package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@JsonTest
public class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    public BookingDtoTest(@Autowired JacksonTester<BookingDto> json) {
        this.json = json;
    }

    User owner = new User("alex", "alex@user.ru");
    User booker = new User("ann", "ann@user.ru");
    Item item = new Item("item", "description", true, owner, null);

    @Test
    void testJsonBookingDto() throws Exception {
        booker.setId(1L);
        item.setId(2L);
        BookingDto bookingDto = new BookingDto(
                LocalDateTime.of(2023, 10, 2, 3, 4, 5),
                LocalDateTime.of(2023, 11, 2, 3, 4, 5),
                item,
                booker,
                booker.getId(),
                WAITING);
        bookingDto.setId(3L);
        JsonContent<BookingDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-02T03:04:05");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-11-02T03:04:05");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("ann");
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }
}