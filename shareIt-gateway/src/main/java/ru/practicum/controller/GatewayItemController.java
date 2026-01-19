package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
import ru.practicum.client.ShareItItemClient;
import ru.practicum.item.dto.CommentRequestDto;
import ru.practicum.item.dto.CommentResponseDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemResponseDto;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class GatewayItemController {

    private final ShareItItemClient shareItClient;

    @PostMapping()
    public ItemDto createItem(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        return shareItClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                              @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return shareItClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return shareItClient.getItem(itemId);
    }

    @GetMapping
    public List<ItemResponseDto> getUserItemList(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId) {
        return shareItClient.getItemListByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItem(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                            @RequestParam(required = false) String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return shareItClient.searchItem(userId, text);
    }

    @PostMapping("{itemId}/comment")
    public CommentResponseDto createComment(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                            @PathVariable Long itemId, @RequestBody CommentRequestDto commentRequestDto) {
        return shareItClient.createComment(userId, itemId, commentRequestDto);
    }
}
