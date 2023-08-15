package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemForAnswerDto {
    private Long id;
    private String name;
    private String description;
    private Long requestId;
    private Boolean available;

    public ItemForAnswerDto(String name, String description, Long requestId, Boolean available) {
        this.name = name;
        this.description = description;
        this.requestId = requestId;
        this.available = available;
    }
}
