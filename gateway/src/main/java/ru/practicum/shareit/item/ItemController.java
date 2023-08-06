package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.FullItemDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Constants.USER_ID;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;


    @GetMapping
    public ResponseEntity<Object> getItemsByOwner(@RequestHeader(USER_ID) Long ownerId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(required = false) Integer size) {
        return itemClient.getItemsByOwner(ownerId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID) Long userId,
                                         @RequestBody @Valid FullItemDto fullItemDto) {
        return itemClient.create(userId, fullItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(USER_ID) Long userId,
                                              @PathVariable Long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @ResponseBody
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody FullItemDto fullItemDto, @PathVariable Long itemId,
                                         @RequestHeader(USER_ID) Long userId) {
        return itemClient.update(fullItemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@PathVariable Long itemId, @RequestHeader(USER_ID) Long ownerId) {
        return itemClient.delete(itemId, ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsBySearchQuery(@RequestParam String text,
                                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(required = false) Integer size) {
        return itemClient.getItemsBySearchQuery(text, from, size);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestBody @Valid CommentDto commentDto,
                                                @RequestHeader(USER_ID) Long userId,
                                                @PathVariable Long itemId) {
        return itemClient.createComment(commentDto, itemId, userId);
    }
}
