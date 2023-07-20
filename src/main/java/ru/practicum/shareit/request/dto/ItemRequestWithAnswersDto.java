package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemForAnswerDto;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestWithAnswersDto {
    private String description;
    private LocalDate created;
    List<ItemForAnswerDto> answers;
}
