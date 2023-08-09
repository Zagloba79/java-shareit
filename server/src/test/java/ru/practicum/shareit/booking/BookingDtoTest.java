package ru.practicum.shareit.booking;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import org.springframework.boot.test.json.JsonContent;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@JsonTest
public class BookingDtoTest {
    private final JacksonTester<BookingDto> json;
    private final Validator validator;

    public BookingDtoTest(@Autowired JacksonTester<BookingDto> json) {
        this.json = json;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private final User owner = new User("alex", "alex@user.ru");
    private final User booker = new User(1L, "ann", "ann@user.ru");
    private final Item item = new Item(2L, "item", "description", true, owner, null);
    final BookingDto bookingDto = new BookingDto(
            3L,
            LocalDateTime.of(2023, 10, 2, 3, 4, 5),
            LocalDateTime.of(2023, 11, 2, 3, 4, 5),
            item,
            booker,
            booker.getId(),
            WAITING);

    @Test
    public void testJsonBookingDto() throws Exception {
        JsonContent<BookingDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-02T03:04:05");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-11-02T03:04:05");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("ann");
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

    @Test
    @DirtiesContext
    public void whenBookingDtoIsValidTest() {
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DirtiesContext
    public void whenStartIsNullTest() {
        bookingDto.setStart(null);
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
    }

    @Test
    @DirtiesContext
    public void whenEndIsNullTest() {
        bookingDto.setEnd(null);
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='may not be null'");
    }

    @Test
    @DirtiesContext
    public void whenItemIsNullTest() {
        bookingDto.setItem(null);
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
    }

    @Test
    @DirtiesContext
    public void whenBookerIsNullTest() {
        bookingDto.setBooker(null);
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
    }

    @Test
    @DirtiesContext
    public void whenStatusIsNullTest() {
        bookingDto.setStatus(null);
        Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
    }
}