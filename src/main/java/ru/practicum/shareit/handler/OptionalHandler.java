package ru.practicum.shareit.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionalHandler {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository requestRepository;
    private final BookingRepository bookingRepository;

    public User getUserFromOpt(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new ObjectNotFoundException("Пользователя с " + id + " не существует.");
        }
        return userOpt.get();
    }

    public Item getItemFromOpt(Long id) {
        Optional<Item> itemOpt = itemRepository.findById(id);
        if (itemOpt.isEmpty()) {
            throw new ObjectNotFoundException("Предмета с " + id + " не существует.");
        }
        return itemOpt.get();
    }

    public ItemRequest getRequestFromOpt(Long id) {
        Optional<ItemRequest> requestOpt = requestRepository.findById(id);
        if (requestOpt.isEmpty()) {
            throw new ObjectNotFoundException("Запроса с " + id + " не существует.");
        }
        return requestOpt.get();
    }

    public Booking getBookingFromOpt(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isEmpty()) {
            throw new ObjectNotFoundException("Букинга с " + id + " не существует.");
        }
        return bookingOpt.get();
    }
}
