package ru.practicum.shareit.booking;

//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.handleAndValidate.EntityHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;

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
    Item item = new Item("name", "Desc", true, owner, null);

//    @Test
//    public void createBooking() {
//        doNothing().when(handler).bookingValidate(newBookingDto, any());
//        when(handler.getItemFromOpt(item.getId())).thenReturn(item);
//    }

}
