//package ru.practicum.shareit.item;
//
//import org.assertj.core.api.AssertionsForClassTypes;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.json.JsonTest;
//import org.springframework.boot.test.json.JacksonTester;
//import org.springframework.boot.test.json.JsonContent;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.practicum.shareit.item.dto.ItemForAnswerDto;
//import ru.practicum.shareit.request.model.ItemRequest;
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
//public class ItemForAnswerDtoTest {
//    private final JacksonTester<ItemForAnswerDto> json;
//    private final Validator validator;
//
//    public ItemForAnswerDtoTest(@Autowired JacksonTester<ItemForAnswerDto> json) {
//        this.json = json;
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    private final User requester = new User("ann", "ann@user.ru");
//    private final ItemRequest request = new ItemRequest("ItemRequest", requester,
//    LocalDateTime.of(2022, 1, 2, 3, 0, 0));
//    ItemForAnswerDto itemForAnswerDto = new ItemForAnswerDto(
//            "item", "description", null, true);
//
//    @BeforeEach
//    public void setUp() {
//        request.setId(1L);
//        itemForAnswerDto.setRequestId(request.getId());
//        itemForAnswerDto.setId(2L);
//    }
//
//    @Test
//    @DirtiesContext
//    public void testJsonItemForAnswerDto() throws Exception {
//        JsonContent<ItemForAnswerDto> result = json.write(itemForAnswerDto);
//        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(2);
//        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
//        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
//        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
//        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenItemForAnswerDtoIsValidTest() {
//        Set<ConstraintViolation<ItemForAnswerDto>> violations = validator.validate(itemForAnswerDto);
//        assertThat(violations).isEmpty();
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenNameIsBlankTest() {
//        itemForAnswerDto.setName("      ");
//        Set<ConstraintViolation<ItemForAnswerDto>> violations = validator.validate(itemForAnswerDto);
//        assertThat(violations).isNotEmpty();
//        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenDescriptionIsBlankTest() {
//        itemForAnswerDto.setDescription("      ");
//        Set<ConstraintViolation<ItemForAnswerDto>> violations = validator.validate(itemForAnswerDto);
//        assertThat(violations).isNotEmpty();
//        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be blank'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenRequestIdIsNullTest() {
//        itemForAnswerDto.setRequestId(null);
//        Set<ConstraintViolation<ItemForAnswerDto>> violations = validator.validate(itemForAnswerDto);
//        assertThat(violations).isNotEmpty();
//        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
//    }
//
//    @Test
//    @DirtiesContext
//    public void whenAvailableIsNullTest() {
//        itemForAnswerDto.setAvailable(null);
//        Set<ConstraintViolation<ItemForAnswerDto>> violations = validator.validate(itemForAnswerDto);
//        assertThat(violations).isNotEmpty();
//        AssertionsForClassTypes.assertThat(violations.toString()).contains("interpolatedMessage='must not be null'");
//    }
//}