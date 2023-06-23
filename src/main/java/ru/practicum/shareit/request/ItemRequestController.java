package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private ItemRequestService itemRequestService;

    @ResponseBody
    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader(USER_ID) int requesterId) {
        return itemRequestService.addRequest(itemRequestDto, requesterId);
    }

    @GetMapping("/{itemRequestId}")
    public ItemRequestDto getItemRequestById(@PathVariable int itemRequestId) {
        return itemRequestService.getRequestById(itemRequestId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByRequester(@RequestHeader(USER_ID) int requesterId) {
        return itemRequestService.getItemsByRequester(requesterId);
    }

    @GetMapping("/{itemId}")
    public List<ItemRequestDto> getItemRequestsByItem(@PathVariable int itemId) {
        return itemRequestService.getItemRequestsByItem(itemId);
    }

    @PatchMapping("/{itemRequestId}")
    public ItemRequestDto update(@RequestBody ItemRequestDto itemRequestDto, @PathVariable int itemId,
                                 @RequestHeader(USER_ID) int requesterId) {
        return itemRequestService.update(itemRequestDto, itemId, requesterId);
    }

    @DeleteMapping("/{itemRequestId}")
    public void delete(@PathVariable int itemId, @RequestHeader(USER_ID) int requesterId) {
        itemRequestService.delete(itemId, requesterId);
    }
}