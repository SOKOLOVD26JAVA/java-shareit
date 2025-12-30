package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Headers;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
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
    public ItemDto createItem(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                              @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemForUserDto> getUserItemList(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId) {
        return userService.getUserItemList(userId);
    }

    @GetMapping("/search")
    public List<ItemForUserDto> searchItem(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                           @RequestParam(required = false) String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.searchItem(userId, text);
    }

    @PostMapping("{itemId}/comment")
    public CommentResponseDto createComment(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                            @PathVariable Long itemId, @RequestBody CommentRequestDto commentRequestDto) {
        return itemService.createComment(userId, itemId, commentRequestDto);
    }
// 2 этих метода по ТЗ должны быть, однако они конфликтуют с методами из предыдущего спринта. Не пойму что с ними делать.
//    @GetMapping("/{itemId}")
//    public List<CommentResponseDto> getCommentsByItemId(@PathVariable Long itemId) {
//        return itemService.getCommentsByItemId(itemId);
//    }
//
//    @GetMapping
//    public List<CommentResponseDto> getUserItemComments(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId) {
//        return  itemService.getUserItemComments(userId);
//    }


}
