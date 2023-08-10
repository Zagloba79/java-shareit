package ru.practicum.shareit.itemRequest;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;
    private final Validator validator;

    public ItemRequestDtoTest(@Autowired JacksonTester<ItemRequestDto> json) {
        this.json = json;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private final UserDto requester = new UserDto(1L, "ann", "ann@user.ru");
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(2L, "desc", requester,
            LocalDateTime.of(2023, 1, 2, 3, 4, 5));


    @Test
    @DirtiesContext
    public void testJsonItemRequestDto() throws Exception {
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-01-02T03:04:05");
        assertThat(result).extractingJsonPathStringValue("$.requester.name").isEqualTo("ann");
    }

    @Test
    @DirtiesContext
    public void whenItemRequestDtoIsValidTest() {
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DirtiesContext
    public void whenDescriptionIsBlankTest() {
        itemRequestDto.setDescription("   ");
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
    }

    @Test
    @DirtiesContext
    public void whenCreatedIsNullTest() {
        itemRequestDto.setCreated(null);
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);
        assertThat(violations).isNotEmpty();
        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
    }
}
