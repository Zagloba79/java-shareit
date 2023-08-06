package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader(Constants.USER_ID) Long requesterId) {
        return itemRequestService.create(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<RequestWithItemsDto> getAllRequestsByRequester(
            @RequestHeader(Constants.USER_ID) Long userId) {
        return itemRequestService.getRequestsByRequester(userId);
    }

    @GetMapping("/all")
    public List<RequestWithItemsDto> getRequestsPageable(
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size,
            @RequestHeader(Constants.USER_ID) Long userId) {
        return itemRequestService.getRequestsPageable(userId, from, size);
    }

    @GetMapping("/{itemRequestId}")
    public RequestWithItemsDto getRequestById(@PathVariable Long itemRequestId,
                                              @RequestHeader(Constants.USER_ID) Long requesterId) {
        return itemRequestService.getRequestById(itemRequestId, requesterId);
    }
}