package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestWithItemsDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    List<ItemForAnswerDto> items;

    public RequestWithItemsDto(String description, LocalDateTime created, List<ItemForAnswerDto> items) {
        this.description = description;
        this.created = created;
        this.items = items;
    }
}