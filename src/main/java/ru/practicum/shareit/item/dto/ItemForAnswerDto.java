package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ItemForAnswerDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Long requestId;
    @NotNull
    private Boolean available;

    public ItemForAnswerDto(String name, String description, Long requestId, Boolean available) {
        this.name = name;
        this.description = description;
        this.requestId = requestId;
        this.available = available;
    }
}
