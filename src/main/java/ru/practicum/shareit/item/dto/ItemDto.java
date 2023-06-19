package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private boolean available;
    private List<String> requests;

    public ItemDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}