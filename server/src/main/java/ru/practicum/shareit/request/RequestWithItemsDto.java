package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestWithItemsDto {
    private Long id;
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime created;
    List<ItemForAnswerDto> items;

    public RequestWithItemsDto(String description, LocalDateTime created, List<ItemForAnswerDto> items) {
        this.description = description;
        this.created = created;
        this.items = items;
    }
}