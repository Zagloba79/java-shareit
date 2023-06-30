package ru.practicum.shareit.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.dto.FeedbackMapper;
import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackStorage feedbackStorage;
    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public FeedbackDto createFeedback(FeedbackDto feedbackDto, Integer bookerId, Integer itemId) {
        Feedback feedback = new Feedback();
        User author = userService.userFromStorage(bookerId);
        Item item = itemStorage.getItemById(itemId).orElseThrow(() -> new ObjectNotFoundException("Данного предмета в базе не существует."));
        for (Booking booking : bookingStorage.getBookingsByBooker(bookerId)) {
            if (booking.getItem().getId().equals(item.getId()) && item.getAvailable().equals(true)) {
                feedback.setItem(item);
                feedback.setOwner(item.getOwner());
                feedback.setAuthor(author);
                feedback.setComment(feedbackDto.getComment());
                feedbackStorage.create(feedback);
            }
        }
        return FeedbackMapper.createFeedbackDto(feedback);
    }

    @Override
    public FeedbackDto getFeedbackById(Integer id) {
        Feedback feedback = feedbackStorage.getFeedback(id).orElseThrow(() -> new ObjectNotFoundException("Отзыва с номером = " + id + " не существует."));
        return FeedbackMapper.createFeedbackDto(feedback);
    }

    @Override
    public List<Feedback> getFeedbacksByItem(Integer itemId) {
        Item item = itemStorage.getItemById(itemId).orElseThrow(() -> new ObjectNotFoundException("Данного предмета в базе не существует."));
        return feedbackStorage.findAll().stream().filter(feedback -> feedback.getItem().getId().equals(item.getId())).collect(toList());
    }

    @Override
    public List<Feedback> getFeedbacksByBooker(Integer bookerId) {
        User booker = userService.userFromStorage(bookerId);
        return feedbackStorage.findAll().stream().filter(feedback -> feedback.getAuthor().getId().equals(booker.getId())).collect(toList());
    }
}