package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Headers;
import ru.practicum.client.ShareItItemRequestClient;
import ru.practicum.itemRequest.dto.ItemRequestDto;
import ru.practicum.itemRequest.dto.ItemRequestWithOutResponseDto;
import ru.practicum.itemRequest.dto.ItemRequestWithResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class GatewayItemRequestController {

    private final ShareItItemRequestClient shareItClient;

    @PostMapping
    public ItemRequestWithOutResponseDto createItemRequest(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                                           @RequestBody ItemRequestDto itemRequestDto) {
        return shareItClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestWithResponseDto> getUserItemRequests(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId) {
        return shareItClient.getUserItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponseDto getItemRequestById(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId, @PathVariable Long requestId) {
        return shareItClient.getItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithResponseDto> getAllItemRequests(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId) {
        return shareItClient.getAllItemRequests(userId);
    }

}