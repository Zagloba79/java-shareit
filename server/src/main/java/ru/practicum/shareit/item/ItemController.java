package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentsAndBookingsDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(
            @RequestBody ItemDto itemDto,
            @RequestHeader(Constants.USER_ID) Long ownerId) {
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestBody ItemDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(Constants.USER_ID) Long ownerId) {
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemWithCommentsAndBookingsDto getItemById(@PathVariable Long itemId,
                                                      @RequestHeader(Constants.USER_ID) Long ownerId) {
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemWithCommentsAndBookingsDto> getItemsByOwnerPageable(
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @RequestHeader(Constants.USER_ID) Long ownerId) {
        return itemService.getItemsByOwnerPageable(ownerId, from, size);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId,
                           @RequestHeader(Constants.USER_ID) Long ownerId) {
        itemService.deleteItem(itemId, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @RequestParam String text,
            @RequestHeader(Constants.USER_ID) Long ownerId) {
        return itemService.getItemsByQueryPageable(from, size, text, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto,
                                    @RequestHeader(Constants.USER_ID) Long authorId,
                                    @PathVariable Long itemId) {
        return itemService.createComment(commentDto, authorId, itemId);
    }
}
