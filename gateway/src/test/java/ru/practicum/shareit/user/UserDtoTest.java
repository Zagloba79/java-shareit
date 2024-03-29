package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

import javax.validation.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserDtoTest {
    private final JacksonTester<UserDto> json;
    private UserDto userDto;
    private final Validator validator;

    public UserDtoTest(@Autowired JacksonTester<UserDto> json) {
        this.json = json;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void beforeEach() {
        userDto = new UserDto(1L, "alex", "alex@user.ru");
    }

    @Test
    public void testJsonUserDto() throws Exception {
        JsonContent<UserDto> result = json.write(userDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("alex");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("alex@user.ru");
    }

    @Test
    public void whenUserDtoIsValidTest() {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).isEmpty();
    }

    @Test
    public void whenUserDtoNameIsBlankTest() {
        userDto.setName("    ");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
    }

    @Test
    public void whenUserDtoEmailIsBlankTest() {
        userDto.setEmail("    ");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).isNotEmpty();
        assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
    }

    @Test
    public void whenUserDtoEmailDoNotHaveATTest() {
        userDto.setEmail("alex.user");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertThat(violations).isNotEmpty();
        System.out.println(violations.toString());
        assertThat(violations.toString()).contains("interpolatedMessage='must be a well-formed email address'");
    }
}