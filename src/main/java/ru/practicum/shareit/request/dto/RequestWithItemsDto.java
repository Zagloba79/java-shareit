package ru.practicum.shareit.request.dto;

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
public class RequestWithItemsDto {
    private Long id;
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime created;
    List<ItemForAnswerDto> answers;
}