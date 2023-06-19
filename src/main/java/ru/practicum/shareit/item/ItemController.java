package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.FeedbackService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private FeedbackService feedbackService;

    @Autowired
    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @ResponseBody
    @PostMapping
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/{items}")
    public List<ItemDto> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.getItemsByOwner(ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable int itemId,
                          @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.update(itemDto, itemId, ownerId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        itemService.deleteItem(itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestParam String text) {
        return itemService.getItemsByQuery(text);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public FeedbackDto createFeedback(@RequestBody FeedbackDto feedbackDto,
                                      @RequestHeader("X-Sharer-User-Id") int bookerId,
                                      @PathVariable int itemId) {
        return feedbackService.createFeedback(feedbackDto, bookerId, itemId);
    }
}