package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.OperationIsNotSupported;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookingServiceITTest {
    private final BookingService service;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository bookingRepository;
    private UserDto abuserDto = new UserDto("abuser", "abuser@user.ru");
    private UserDto ownerDto = new UserDto("owner", "owner@user.ru");
    private UserDto bookerDto = new UserDto("booker", "booker@user.ru");
    private ItemDto itemDto = new ItemDto("Item", "Description",
            true, null);

    @Test
    @DirtiesContext
    public void exceptionWhenCreateBookingByOwnerItemTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 12, 25, 0, 0, 0),
                LocalDateTime.of(2023, 12, 25, 1, 0, 0),
                itemDto.getId());
        OperationIsNotSupported exception = assertThrows(OperationIsNotSupported.class,
                () -> service.create(newBookingDto, ownerDto.getId()));
        assertEquals("Владелец не может быть букером", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenUpdateBookingTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        Long id = 123L;
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> service.update(id, ownerDto.getId(), true));
        assertEquals("Букинга с bookingId = 123 и с ownerId = " + ownerDto.getId() + " не существует.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenCreateBookingWhenItemIsNotAvailableTest() {
        ownerDto = userService.create(ownerDto);
        ItemDto itemDto = new ItemDto("Item", "Description",
                false, null);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 12, 25, 0, 0, 0),
                LocalDateTime.of(2023, 12, 25, 1, 0, 0),
                itemDto.getId());
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.create(newBookingDto, ownerDto.getId()));
        assertEquals("Этот предмет не сдаётся в аренду", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenDatesIsPastTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2013, 12, 25, 0, 0, 0),
                LocalDateTime.of(2013, 12, 25, 1, 0, 0),
                itemDto.getId());
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.create(newBookingDto, ownerDto.getId()));
        assertEquals("даты в прошлом", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenWrongDatesTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 12, 25, 0, 0, 0),
                LocalDateTime.of(2023, 10, 25, 1, 0, 0),
                itemDto.getId());
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.create(newBookingDto, ownerDto.getId()));
        assertEquals("даты попутаны", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenStartIsNullTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                null,
                LocalDateTime.of(2023, 10, 25, 1, 0, 0),
                itemDto.getId());
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.create(newBookingDto, ownerDto.getId()));
        assertEquals("даты не заполнены", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenEndIsNullTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 10, 25, 1, 0, 0),
                null,
                itemDto.getId());
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.create(newBookingDto, ownerDto.getId()));
        assertEquals("даты не заполнены", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenBookingIsExpiredTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                itemDto.getId());
        BookingDto bookingDto = service.create(newBookingDto, bookerDto.getId());
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.update(bookingDto.getId(), ownerDto.getId(), false));
        assertEquals("Время бронирования уже истекло!", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenBookingIsConfirmedTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 12, 25, 0, 0, 0),
                LocalDateTime.of(2023, 12, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto = service.create(newBookingDto, bookerDto.getId());
        service.update(bookingDto.getId(), ownerDto.getId(), true);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.update(bookingDto.getId(), ownerDto.getId(), false));
        assertEquals("Решение по бронированию уже принято!", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenBookingConfirmedByBookerTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 12, 25, 0, 0, 0),
                LocalDateTime.of(2023, 12, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto = service.create(newBookingDto, bookerDto.getId());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> service.update(bookingDto.getId(), bookerDto.getId(), true));
        assertEquals("Букинга с bookingId = " + bookingDto.getId() +
                " и с ownerId = " + bookerDto.getId() + " не существует.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenGetBookingByOtherUserTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        abuserDto = userService.create(abuserDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 12, 25, 0, 0, 0),
                LocalDateTime.of(2023, 12, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto = service.create(newBookingDto, bookerDto.getId());
        OperationIsNotSupported exception = assertThrows(OperationIsNotSupported.class,
                () -> service.getBookingById(bookingDto.getId(), abuserDto.getId()));
        assertEquals("Вы - левый чувак", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenOtherUserConfirmsBookingTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        abuserDto = userService.create(abuserDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 12, 25, 0, 0, 0),
                LocalDateTime.of(2023, 12, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto = service.create(newBookingDto, bookerDto.getId());
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> service.update(bookingDto.getId(), abuserDto.getId(), true));
        assertEquals("Букинга с bookingId = " + bookingDto.getId() +
                " и с ownerId = " + abuserDto.getId() + " не существует.", exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingsWhenGetBookingsByBookerAndStateIsAllTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        abuserDto = userService.create(abuserDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 25, 0, 0, 0),
                LocalDateTime.of(2023, 8, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        List<BookingDto> bookingsByOwner = service.getBookingsByOwnerAndState(0, 10,
                "ALL", ownerDto.getId());
        assertEquals(2, bookingsByOwner.size());
        List<BookingDto> bookingsByBooker = service.getBookingsByBookerAndState(0, 10,
                "ALL", bookerDto.getId());
        assertEquals(2, bookingsByBooker.size());
        List<BookingDto> bookingsByUser = service.getBookingsByOwnerAndState(0, 10,
                "ALL", abuserDto.getId());
        assertEquals(0, bookingsByUser.size());
        List<BookingDto> bookingsByUser2 = service.getBookingsByBookerAndState(0, 10,
                "ALL", abuserDto.getId());
        assertEquals(0, bookingsByUser2.size());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingsWhenGetBookingsWhenStateIsWaitingTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        abuserDto = userService.create(abuserDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 25, 0, 0, 0),
                LocalDateTime.of(2023, 8, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        List<BookingDto> bookingsByOwner = service.getBookingsByOwnerAndState(0, 10,
                "WAITING", ownerDto.getId());
        assertEquals(1, bookingsByOwner.size());
        List<BookingDto> bookingsByBooker = service.getBookingsByBookerAndState(0, 10,
                "WAITING", bookerDto.getId());
        assertEquals(1, bookingsByBooker.size());
        List<BookingDto> bookingsByUser = service.getBookingsByOwnerAndState(0, 10,
                "WAITING", abuserDto.getId());
        assertEquals(0, bookingsByUser.size());
        List<BookingDto> bookingsByUser2 = service.getBookingsByBookerAndState(0, 10,
                "WAITING", abuserDto.getId());
        assertEquals(0, bookingsByUser2.size());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingsWhenGetBookingsWhenStateIsRejectedTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        abuserDto = userService.create(abuserDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 25, 0, 0, 0),
                LocalDateTime.of(2023, 8, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        service.update(bookingDto1.getId(), ownerDto.getId(), false);
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        List<BookingDto> bookingsByOwner = service.getBookingsByOwnerAndState(0, 10,
                "REJECTED", ownerDto.getId());
        assertEquals(1, bookingsByOwner.size());
        List<BookingDto> bookingsByBooker = service.getBookingsByBookerAndState(0, 10,
                "REJECTED", bookerDto.getId());
        assertEquals(1, bookingsByBooker.size());
        List<BookingDto> bookingsByUser = service.getBookingsByOwnerAndState(0, 10,
                "REJECTED", abuserDto.getId());
        assertEquals(0, bookingsByUser.size());
        List<BookingDto> bookingsByUser2 = service.getBookingsByBookerAndState(0, 10,
                "REJECTED", abuserDto.getId());
        assertEquals(0, bookingsByUser2.size());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenGetBookingsAndUnknownStateTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        abuserDto = userService.create(abuserDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 25, 0, 0, 0),
                LocalDateTime.of(2023, 8, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        service.update(bookingDto1.getId(), ownerDto.getId(), false);
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        String state = "UNKNOWN";
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.getBookingsByOwnerAndState(0, 10, state, ownerDto.getId()));
        assertEquals("Unknown state: " + state, exception.getMessage());
        exception = assertThrows(ValidationException.class,
                () -> service.getBookingsByBookerAndState(0, 10, state, bookerDto.getId()));
        assertEquals("Unknown state: " + state, exception.getMessage());
        exception = assertThrows(ValidationException.class,
                () -> service.getBookingsByOwnerAndState(0, 10, state, abuserDto.getId()));
        assertEquals("Unknown state: " + state, exception.getMessage());
        exception = assertThrows(ValidationException.class,
                () -> service.getBookingsByBookerAndState(0, 10, state, abuserDto.getId()));
        assertEquals("Unknown state: " + state, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void exceptionWhenGetBookingsByOwnerAndUnknownStateTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 25, 0, 0, 0),
                LocalDateTime.of(2023, 8, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        service.update(bookingDto1.getId(), ownerDto.getId(), false);
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        String state = "UNKNOWN";
        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.getBookingsByOwnerAndState(0, 10, state, ownerDto.getId()));
        assertEquals("Unknown state: " + state, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingsWhenGetBookingsByOwnerAndStateIsCurrentTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.of(2023, 8, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        service.update(bookingDto1.getId(), ownerDto.getId(), true);
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        List<BookingDto> bookings = service.getBookingsByOwnerAndState(0, 10,
                "CURRENT", ownerDto.getId());
        assertEquals(1, bookings.size());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingsWhenGetBookingsByBookerAndStateIsCurrentTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.of(2023, 8, 25, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        service.update(bookingDto1.getId(), ownerDto.getId(), true);
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        List<BookingDto> bookings = service.getBookingsByBookerAndState(0, 10,
                "CURRENT", bookerDto.getId());
        assertEquals(1, bookings.size());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingsWhenGetBookingsWhenStateIsPastTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        service.update(bookingDto1.getId(), ownerDto.getId(), true);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        List<BookingDto> bookings = service.getBookingsByBookerAndState(0, 10,
                "PAST", bookerDto.getId());
        assertEquals(1, bookings.size());
        List<BookingDto> bookings2 = service.getBookingsByOwnerAndState(0, 10,
                "PAST", ownerDto.getId());
        assertEquals(1, bookings2.size());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingsWhenGetBookingsWhenStateIsFutureTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto fBookingDto = new NewBookingDto(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                itemDto.getId());
        BookingDto bookingDto1 = service.create(fBookingDto, bookerDto.getId());
        service.update(bookingDto1.getId(), ownerDto.getId(), true);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        NewBookingDto sBookingDto = new NewBookingDto(
                LocalDateTime.of(2023, 8, 26, 0, 0, 0),
                LocalDateTime.of(2023, 8, 26, 1, 0, 0),
                itemDto.getId());
        BookingDto bookingDto2 = service.create(sBookingDto, bookerDto.getId());
        service.update(bookingDto2.getId(), ownerDto.getId(), true);
        List<BookingDto> bookings = service.getBookingsByBookerAndState(0, 10,
                "FUTURE", bookerDto.getId());
        assertEquals(1, bookings.size());
        List<BookingDto> bookings2 = service.getBookingsByOwnerAndState(0, 10,
                "FUTURE", ownerDto.getId());
        assertEquals(1, bookings2.size());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingWhenUpdateBookingByOwnerTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                itemDto.getId());
        BookingDto bookingDto = service.create(newBookingDto, bookerDto.getId());
        bookingDto = service.update(bookingDto.getId(), ownerDto.getId(), true);
        assertEquals(itemDto.getName(), bookingDto.getItem().getName());
        assertEquals(bookerDto.getId(), bookingDto.getBooker().getId());
        assertEquals(bookerDto.getName(), bookingDto.getBooker().getName());
        assertEquals(APPROVED, bookingDto.getStatus());
    }

    @Test
    @DirtiesContext
    public void shouldReturnBookingWhenGetBookingByIdTest() {
        ownerDto = userService.create(ownerDto);
        itemDto = itemService.create(itemDto, ownerDto.getId());
        bookerDto = userService.create(bookerDto);
        abuserDto = userService.create(abuserDto);
        NewBookingDto newBookingDto = new NewBookingDto(
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                itemDto.getId());
        BookingDto bookingDto = service.create(newBookingDto, bookerDto.getId());
        BookingDto bookingByIdDto = service.getBookingById(bookingDto.getId(), ownerDto.getId());
        assertEquals(itemDto.getName(), bookingByIdDto.getItem().getName());
        assertEquals(bookerDto.getId(), bookingByIdDto.getBooker().getId());
        assertEquals(bookerDto.getName(), bookingByIdDto.getBooker().getName());
        bookingByIdDto = service.getBookingById(bookingDto.getId(), bookerDto.getId());
        assertEquals(itemDto.getName(), bookingByIdDto.getItem().getName());
        assertEquals(bookerDto.getId(), bookingByIdDto.getBooker().getId());
        assertEquals(bookerDto.getName(), bookingByIdDto.getBooker().getName());
        OperationIsNotSupported exception = assertThrows(OperationIsNotSupported.class,
                () -> service.getBookingById(bookingDto.getId(), abuserDto.getId()));
        assertEquals("Вы - левый чувак", exception.getMessage());
    }
}
