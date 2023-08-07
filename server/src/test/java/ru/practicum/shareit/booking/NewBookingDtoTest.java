//package ru.practicum.shareit.booking;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.json.JsonTest;
//import org.springframework.boot.test.json.JacksonTester;
//import org.springframework.boot.test.json.JsonContent;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.practicum.shareit.booking.dto.NewBookingDto;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.user.model.User;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import javax.validation.ValidatorFactory;
//import java.time.LocalDateTime;
//import java.util.Set;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
//@JsonTest
//public class NewBookingDtoTest {
//    private final JacksonTester<NewBookingDto> json;
//    private final Validator validator;
//    private final User owner = new User("alex", "alex@user.ru");
//    private final Item item = new Item("item", "description", true, owner, null);
//    private final NewBookingDto newBookingDto = new NewBookingDto(
//            LocalDateTime.of(2023, 10, 11, 12, 13, 14),
//            LocalDateTime.of(2023, 11, 12, 13, 14, 15),
//            1L);
//
//    public NewBookingDtoTest(@Autowired JacksonTester<NewBookingDto> json) {
//        this.json = json;
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    @DirtiesContext
//    public void testJsonNewBookingDtoTest() throws Exception {
//        item.setId(1L);
//        JsonContent<NewBookingDto> result = json.write(newBookingDto);
//        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-11T12:13:14");
//        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-11-12T13:14:15");
//        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenNewBookingDtoIsValidTest() {
//        Set<ConstraintViolation<NewBookingDto>> violations = validator.validate(newBookingDto);
//        assertThat(violations).isEmpty();
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenItemIdIsNullTest() {
//        newBookingDto.setItemId(null);
//        Set<ConstraintViolation<NewBookingDto>> violations = validator.validate(newBookingDto);
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenStartIsNullTest() {
//        item.setId(1L);
//        newBookingDto.setStart(null);
//        Set<ConstraintViolation<NewBookingDto>> violations = validator.validate(newBookingDto);
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenEndIsNullTest() {
//        item.setId(1L);
//        newBookingDto.setEnd(null);
//        Set<ConstraintViolation<NewBookingDto>> violations = validator.validate(newBookingDto);
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenStartBeforeNowTest() {
//        item.setId(1L);
//        newBookingDto.setStart(LocalDateTime.now().minusSeconds(1));
//        Set<ConstraintViolation<NewBookingDto>> violations = validator.validate(newBookingDto);
//        System.out.println(violations);
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.toString()).contains("interpolatedMessage='must be " +
//                "a date in the present or in the future'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenEndBeforeNowTest() {
//        item.setId(1L);
//        newBookingDto.setEnd(LocalDateTime.now().minusSeconds(1));
//        Set<ConstraintViolation<NewBookingDto>> violations = validator.validate(newBookingDto);
//        assertThat(violations).isNotEmpty();
//        assertThat(violations.toString()).contains("interpolatedMessage='must be a future date'");
//    }
//}
