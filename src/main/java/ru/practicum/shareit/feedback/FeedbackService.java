package ru.practicum.shareit.feedback;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@Slf4j
@AllArgsConstructor
public class FeedbackService {
    private FeedbackStorage feedbackStorage;

    public List<Feedback> getFeedbacksByItem(Item item) {
        int id = item.getId();
        return feedbackStorage.findAll().stream()
                .filter(feedback -> feedback.getItem().getId() == id)
                .collect(toList());
    }

    public List<Feedback> getFeedbacksByBooker(User booker) {
        int id = booker.getId();
        return feedbackStorage.findAll().stream()
                .filter(feedback -> feedback.getBooker().getId() == id)
                .collect(toList());
    }
}
