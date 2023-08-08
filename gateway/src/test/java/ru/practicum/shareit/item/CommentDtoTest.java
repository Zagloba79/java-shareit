package ru.practicum.shareit.item;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.CommentDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoTest {
    private final JacksonTester<CommentDto> json;
    private final Validator validator;
    private final CommentDto commentDto = new CommentDto(1L, "text", "alex",
            LocalDateTime.of(2023, 11, 12, 13, 14, 15));

    public CommentDtoTest(@Autowired JacksonTester<CommentDto> json) {
        this.json = json;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DirtiesContext
    public void testJsonCommentDto() throws Exception {
        commentDto.setId(1L);
        JsonContent<CommentDto> result = json.write(commentDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("alex");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-11-12T13:14:15");
    }

    @Test
    @DirtiesContext
    public void whenNewBookingDtoIsValidTest() {
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DirtiesContext
    public void whenCommentDtoTextIsBlank() {
        commentDto.setText("   ");
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
    }
}