package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.FullItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
    private UserDto requester;
    private LocalDateTime created;
    private List<FullItemDto> items;
}
