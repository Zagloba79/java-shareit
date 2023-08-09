package ru.practicum.shareit.booking;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookItemRequestDtoTest {
    private final JacksonTester<BookItemRequestDto> json;
    private final Validator validator;
    BookItemRequestDto bookingDto = new BookItemRequestDto(
            1L,
            LocalDateTime.of(2023, 10, 11, 12, 13, 14),
            LocalDateTime.of(2023, 11, 12, 13, 14, 15));

    public BookItemRequestDtoTest(@Autowired JacksonTester<BookItemRequestDto> json) {
        this.json = json;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DirtiesContext
    public void testJsonBookingDto() throws Exception {
        JsonContent<BookItemRequestDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-11T12:13:14");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-11-12T13:14:15");
    }

    @Test
    @DirtiesContext
    public void whenNewBookingDtoIsValidTest() {
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DirtiesContext
    public void whenStartIsInPast() {
        bookingDto.setStart(LocalDateTime.now().minusHours(12));
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("{javax.validation.constraints.FutureOrPresent.message}");
    }

    @Test
    @DirtiesContext
    public void whenStartIsNow() {
        bookingDto.setStart(LocalDateTime.now());
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("{javax.validation.constraints.FutureOrPresent.message}");
    }

    @Test
    @DirtiesContext
    public void whenEndIsInPast() {
        bookingDto.setEnd(LocalDateTime.now().minusHours(12));
        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(bookingDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("{javax.validation.constraints.Future.message}");
    }
}
