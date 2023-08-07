//package ru.practicum.shareit.item;
//
//import org.assertj.core.api.AssertionsForClassTypes;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.json.JsonTest;
//import org.springframework.boot.test.json.JacksonTester;
//import org.springframework.boot.test.json.JsonContent;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.practicum.shareit.booking.dto.BookingForDataDto;
//import ru.practicum.shareit.item.dto.ItemWithCommentsAndBookingsDto;
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
//public class ItemWithCommentsAndBookingsDtoTest {
//    private final JacksonTester<ItemWithCommentsAndBookingsDto> json;
//    private final Validator validator;
//
//    public ItemWithCommentsAndBookingsDtoTest(@Autowired JacksonTester<ItemWithCommentsAndBookingsDto> json) {
//        this.json = json;
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    private final User booker = new User(1L, "ann", "ann@user.ru");
//    BookingForDataDto lastBooking = new BookingForDataDto(
//            2L, booker.getId(),
//            LocalDateTime.of(2023, 10, 11, 12, 13, 14),
//            LocalDateTime.of(2023, 11, 12, 13, 14, 15));
//    BookingForDataDto nextBooking = new BookingForDataDto(
//            3L, booker.getId(),
//            LocalDateTime.of(2023, 10, 11, 12, 13, 14),
//            LocalDateTime.of(2023, 11, 12, 13, 14, 15));
//    ItemWithCommentsAndBookingsDto itemWithDto = new ItemWithCommentsAndBookingsDto(
//            4L, "itemWith", "description", true,
//            null, lastBooking, nextBooking);
//
//    @Test
//    public void testJsonItemWithCommentsAndBookingsDto() throws Exception {
//        JsonContent<ItemWithCommentsAndBookingsDto> result = json.write(itemWithDto);
//        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(4);
//        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("itemWith");
//        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
//        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
//        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(2);
//        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(3);
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenItemWithCommentsAndBookingsDtoIsValidTest() {
//        Set<ConstraintViolation<ItemWithCommentsAndBookingsDto>> violations = validator.validate(itemWithDto);
//        assertThat(violations).isEmpty();
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenNameIsBlankTest() {
//        itemWithDto.setName("      ");
//        Set<ConstraintViolation<ItemWithCommentsAndBookingsDto>> violations = validator.validate(itemWithDto);
//        assertThat(violations).isNotEmpty();
//        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenDescriptionIsBlankTest() {
//        itemWithDto.setDescription("      ");
//        Set<ConstraintViolation<ItemWithCommentsAndBookingsDto>> violations = validator.validate(itemWithDto);
//        assertThat(violations).isNotEmpty();
//        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenAvailableIsNullTest() {
//        itemWithDto.setAvailable(null);
//        Set<ConstraintViolation<ItemWithCommentsAndBookingsDto>> violations = validator.validate(itemWithDto);
//        assertThat(violations).isNotEmpty();
//        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
//    }
//}