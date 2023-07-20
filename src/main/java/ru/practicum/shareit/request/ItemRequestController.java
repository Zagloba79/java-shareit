package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader(USER_ID) Long requesterId) {
        return itemRequestService.addRequest(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestWithAnswersDto> getAllRequestsByRequester(
            @RequestHeader(USER_ID) Long userId) {
        return itemRequestService.getRequestsByRequester(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequestsWithAnswersInQuantity(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam Integer size,
            @RequestHeader(USER_ID) Long userId) {
        return itemRequestService.getRequestsInQuantity(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswersDto getRequestById(@PathVariable Long itemRequestId,
                                             @RequestHeader(USER_ID) Long requesterId) {
        return itemRequestService.getRequestById(itemRequestId, requesterId);
    }

    @PatchMapping("/{requestId}")
    public ItemRequestDto update(@RequestBody ItemRequestDto itemRequestDto, @PathVariable Long itemId,
                                 @RequestHeader(USER_ID) Long requesterId) {
        return itemRequestService.update(itemRequestDto, itemId, requesterId);
    }

    @DeleteMapping("/{requestId}")
    public void delete(@PathVariable Long itemId, @RequestHeader(USER_ID) Long requesterId) {
        itemRequestService.delete(itemId, requesterId);
    }
}