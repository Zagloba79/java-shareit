package ru.practicum.shareit.booking;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


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
}

