package ru.practicum.shareit.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.practicum.shareit.booking.model.BookingStatus.APPROVED;


@Service
@RequiredArgsConstructor
public class EntityHandler {
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

    public void itemIsAvailable(Item item) {
        if (item.getAvailable() == null || item.getAvailable().equals(false)) {
            throw new ValidationException("Этот предмет не сдаётся в аренду");
        }
    }

    public void bookingValidate(NewBookingDto bookingDto, LocalDateTime presentTime) {
        if (bookingDto.getStart() == null ||
                bookingDto.getEnd() == null) {
            throw new ValidationException("даты не заполнены");
        }
        if (bookingDto.getStart().isBefore(presentTime) ||
                bookingDto.getEnd().isBefore(presentTime)) {
            throw new ValidationException("даты в прошлом");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) ||
                bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("даты попутаны");
        }
    }

    public void itemValidate(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Некорректное название предмета: " + itemDto.getName());
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Некорректное описание предмета: " + itemDto.getDescription());
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Некорректный статус предмета: ");
        }
    }

    public void authorValidate(Long userId, Long itemId) {
        if (bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(
                itemId, userId, LocalDateTime.now(), APPROVED) == null) {
            throw new ValidationException(
                    "Юзер с id = " + userId + " ещё не арендовал предмет с id = " + itemId);
        }
    }
}