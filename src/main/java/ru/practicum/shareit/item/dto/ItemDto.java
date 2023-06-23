package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "request"})
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private boolean available;
    private ItemRequest request;

    public ItemDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}