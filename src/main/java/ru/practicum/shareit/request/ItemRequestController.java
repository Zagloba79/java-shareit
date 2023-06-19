package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private ItemRequestService itemRequestService;

    @ResponseBody
    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.addRequest(itemRequestDto, requesterId);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestById(@PathVariable int itemRequestId) {
        return itemRequestService.getRequestById(itemRequestId);
    }

    @GetMapping("/{itemRequests}")
    public List<ItemRequestDto> getItemRequestsByRequester(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.getItemsByRequester(requesterId);
    }

    @PatchMapping("/{itemRequestId}")
    public ItemRequestDto update(@RequestBody ItemRequestDto itemRequestDto, @PathVariable int itemId,
                                 @RequestHeader("X-Sharer-User-Id") int requesterId) {
        return itemRequestService.update(itemRequestDto, itemId, requesterId);
    }

    @DeleteMapping("/{itemRequestId}")
    public void delete(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int requesterId) {
        itemRequestService.delete(itemId, requesterId);
    }
}