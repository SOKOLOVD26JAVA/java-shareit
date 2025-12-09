package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;


    @PostMapping()
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Integer userId,
                              @Valid @RequestBody ItemDto itemDto) {
        ItemDto returnableItem = itemService.createItem(userId, itemDto);
        userService.addItemToUser(userId, returnableItem);
        return returnableItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Integer userId,
                              @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        userService.getUserByID(userId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable int itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemForUserDto> getUserItemList(@RequestHeader(value = "X-Sharer-User-Id", required = false) int userId) {
        return userService.getUserItemList(userId);
    }

    @GetMapping("/search")
    public List<ItemForUserDto> searchItem(@RequestHeader(value = "X-Sharer-User-Id", required = false) Integer userId,
                                           @RequestParam(defaultValue = "missing") String text) {
        return itemService.searchItem(userId, text);
    }


}
