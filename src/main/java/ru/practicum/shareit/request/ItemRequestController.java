package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

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

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestById(@PathVariable Long itemRequestId,
                                             @RequestHeader(USER_ID) Long requesterId) {
        return itemRequestService.getRequestById(itemRequestId);
    }

    @GetMapping("/{requesterId}")
    public List<ItemRequestDto> getItemRequestsByRequester(@RequestHeader(USER_ID) Long requesterId) {
        return itemRequestService.getItemsByRequester(requesterId);
    }

    @PatchMapping("/{itemRequestId}")
    public ItemRequestDto update(@RequestBody ItemRequestDto itemRequestDto, @PathVariable Long itemId,
                                 @RequestHeader(USER_ID) Long requesterId) {
        return itemRequestService.update(itemRequestDto, itemId, requesterId);
    }

    @DeleteMapping("/{itemRequestId}")
    public void delete(@PathVariable Long itemId, @RequestHeader(USER_ID) Long requesterId) {
        itemRequestService.delete(itemId, requesterId);
    }
}