package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.Constants.USER_ID;


@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @ResponseBody
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                         @RequestHeader(USER_ID) Long requesterId) {

        return itemRequestClient.create(itemRequestDto, requesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("requestId") Long itemRequestId,
                                                     @RequestHeader(USER_ID) Long userId) {
        return itemRequestClient.getItemRequestById(userId, itemRequestId);
    }


    @GetMapping
    public ResponseEntity<Object> getOwnItemRequests(@RequestHeader(USER_ID) Long userId) {
        return itemRequestClient.getOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(USER_ID) Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @RequestParam(required = false) Integer size) {
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }
}