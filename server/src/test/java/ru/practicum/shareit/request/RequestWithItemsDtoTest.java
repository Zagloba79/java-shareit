package ru.practicum.shareit.request;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestWithItemsDtoTest {
    private final JacksonTester<RequestWithItemsDto> json;
    private final Validator validator;


    public RequestWithItemsDtoTest(@Autowired JacksonTester<RequestWithItemsDto> json) {
        this.json = json;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    ItemForAnswerDto item = new ItemForAnswerDto(1L, "name", "desc", 5L, true);
    RequestWithItemsDto requestWithItemsDto = new RequestWithItemsDto(5L, "Description",
            LocalDateTime.of(2023, 10, 11, 12, 13, 14),
            List.of(item));

    @Test
    public void testJsonRequestWithItemsDto() throws Exception {
        JsonContent<RequestWithItemsDto> result = json.write(requestWithItemsDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-10-11T12:13:14");
    }

    @Test
    @DirtiesContext
    public void whenItemRequestDtoIsValidTest() {
        Set<ConstraintViolation<RequestWithItemsDto>> violations = validator.validate(requestWithItemsDto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DirtiesContext
    public void whenDescriptionIsBlankTest() {
        requestWithItemsDto.setDescription("   ");
        Set<ConstraintViolation<RequestWithItemsDto>> violations = validator.validate(requestWithItemsDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
    }

    @Test
    @DirtiesContext
    public void whenCreatedIsNullTest() {
        requestWithItemsDto.setCreated(null);
        Set<ConstraintViolation<RequestWithItemsDto>> violations = validator.validate(requestWithItemsDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
    }
}