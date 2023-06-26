package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.FeedbackService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private ItemService itemService;
    private FeedbackService feedbackService;

    @ResponseBody
    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader(USER_ID) Integer ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Integer itemId, @RequestHeader(USER_ID) Integer ownerId) {
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDto> getItemsByOwner(@RequestHeader(USER_ID) Integer ownerId) {
        if (ownerId != null) {
            return itemService.getItemsByOwner(ownerId);
        }
        return itemService.findAll();
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Integer itemId,
                          @RequestHeader(USER_ID) Integer ownerId) {
        return itemService.update(itemDto, itemId, ownerId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Integer itemId, @RequestHeader(USER_ID) Integer ownerId) {
        itemService.deleteItem(itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestParam String text, @RequestHeader(USER_ID) Integer ownerId) {
        return itemService.getItemsByQuery(text, ownerId);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public FeedbackDto createFeedback(@RequestBody FeedbackDto feedbackDto,
                                      @RequestHeader(USER_ID) Integer bookerId,
                                      @PathVariable Integer itemId) {
        return feedbackService.createFeedback(feedbackDto, bookerId, itemId);
    }
}