package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feedback.FeedbackService;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDatesDto;

import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;
    private final FeedbackService feedbackService;

    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader(USER_ID) Long ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader(USER_ID) Long ownerId) {
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemWithDatesDto> getItemsByOwner(@RequestHeader(USER_ID) Long ownerId) {
        if (ownerId != null) {
            return itemService.getItemsByOwner(ownerId);
        }
        return Collections.EMPTY_LIST;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                          @RequestHeader(USER_ID) Long ownerId) {
        return itemService.update(itemDto, itemId, ownerId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId, @RequestHeader(USER_ID) Long ownerId) {
        itemService.deleteItem(itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestParam String text, @RequestHeader(USER_ID) Long ownerId) {
        return itemService.getItemsByQuery(text, ownerId);
    }

//    @PostMapping("/{itemId}/comment")
//    public Comment createComment(@RequestBody CommentDto commentDto,
//                                      @RequestHeader(USER_ID) Long bookerId,
//                                      @PathVariable Long itemId) {
//        return feedbackService.createFeedback(commentDto, bookerId, itemId);
//    }
}