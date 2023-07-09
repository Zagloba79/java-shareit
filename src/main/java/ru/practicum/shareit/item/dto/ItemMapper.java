package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {
    public static ItemDto createItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static ItemWithCommentsDto createItemWithCommentsDto(Item item, List<Comment> comments) {
        ItemWithCommentsDto itemWithCommentsDto = new ItemWithCommentsDto();
        itemWithCommentsDto.setId(item.getId());
        itemWithCommentsDto.setName(item.getName());
        itemWithCommentsDto.setDescription(item.getDescription());
        itemWithCommentsDto.setAvailable(item.getAvailable());
        itemWithCommentsDto.setComments(comments);
        return itemWithCommentsDto;
    }

    public static Item createItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        return item;
    }
}