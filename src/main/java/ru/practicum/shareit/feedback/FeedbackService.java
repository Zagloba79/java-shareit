package ru.practicum.shareit.feedback;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.dto.FeedbackMapper;
import ru.practicum.shareit.item.InMemoryItemStorage;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;

@Service
@Slf4j
@AllArgsConstructor
public class FeedbackService {
    private FeedbackStorage feedbackStorage;
    private BookingService bookingService;
    private ItemStorage itemStorage;
    private FeedbackMapper feedbackMapper;
    private int feedbackId = 0;

    public List<Feedback> getFeedbacksByItem(Item item) {
        int itemId = item.getId();
        return feedbackStorage.findAll().stream()
                .filter(feedback -> feedback.getItem().getId() == itemId)
                .collect(toList());
    }

    public List<Feedback> getFeedbacksByBooker(User booker) {
        int bookerId = booker.getId();
        return feedbackStorage.findAll().stream()
                .filter(feedback -> feedback.getBooker().getId() == bookerId)
                .collect(toList());
    }

    public void createFeedback(FeedbackDto feedbackDto, int itemId, int bookerId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() ->
                new ObjectNotFoundException("Предмета с " + itemId + " не существует."));
        for (Booking booking : bookingService.getBookingsByBooker(bookerId)) {
            if (booking.getStatuses().contains(APPROVED) && item.isAvailable()) {
                Feedback f = new Feedback(
                        feedbackId++,
                        item,
                        feedbackDto.getOwner(),
                        feedbackDto.getBooker(),
                        feedbackDto.getComment());
                feedbackStorage.create(f.getId(), f);
            }
        }
    }

    public FeedbackDto getFeedbackById(int id) {
        return feedbackMapper.createFeedbackDto(feedbackStorage.getFeedback(id).orElseThrow(() ->
                new ObjectNotFoundException("Отзыва с номером = " + id + " не существует.")));
    }
}
