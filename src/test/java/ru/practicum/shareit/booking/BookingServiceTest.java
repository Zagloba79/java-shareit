package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.OperationIsNotSupported;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.BookingStatus.WAITING;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    BookingServiceImpl service;
    @Mock
    BookingRepository repository;
    @Mock
    EntityHandler handler;
    NewBookingDto newBookingDto = new NewBookingDto();
    User owner = new User("iAm", "iam@user.ru");
    User booker = new User("booker", "booker@user.ru");
    Item item = new Item("name", "Desc", true, owner, null);
//    Booking booking = new Booking()


//    @Test
//    public void createBooking() {
//        doNothing().when(handler).bookingValidate(newBookingDto, any());
//        when(handler.getItemFromOpt(item.getId())).thenReturn(item);
//        doNothing().when(handler).itemIsAvailable(item);
//        when(repository.save(booking)).thenReturn(booking);
//    }
//
//
//    public BookingDto create(NewBookingDto newBookingDto, Long bookerId) {
//        LocalDateTime presentTime = LocalDateTime.now();
//        handler.bookingValidate(newBookingDto, presentTime);
//        Item item = handler.getItemFromOpt(newBookingDto.getItemId());
//        handler.itemIsAvailable(item);
//        User booker = handler.getUserFromOpt(bookerId);
//        if (item.getOwner().getId().equals(bookerId)) {
//            throw new OperationIsNotSupported("Букер не может быть владельцем");
//        }
//        Booking booking = BookingMapper.createNewBooking(
//                newBookingDto.getStart(),
//                newBookingDto.getEnd(),
//                item,
//                booker);
//        booking.setStatus(WAITING);
//        bookingRepository.save(booking);
//        return BookingMapper.createBookingDto(booking);
//    }
}

