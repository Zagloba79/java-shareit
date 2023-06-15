//package ru.practicum.shareit.item;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.model.ItemService;
//
///**
// * TODO Sprint add-controllers.
// */
//@RestController
//@RequestMapping("/items")
//public class ItemController {
//    private final ItemService itemService;
//
//    @Autowired
//    public ItemController(ItemService itemService) {
//        this.itemService = itemService;
//    }
//
//    @PostMapping
//    public Item create(@RequestBody ItemDto itemDto) {
//        return itemService.item;
//    }
//}
