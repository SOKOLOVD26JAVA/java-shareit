package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Headers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithOutResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestWithOutResponseDto createItemRequest(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId,
                                                           @RequestBody ItemRequestDto itemRequestDto) {
        return requestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestWithResponseDto> getUserItemRequests(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId) {
        return requestService.getUserItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponseDto getItemRequestById(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId, @PathVariable Long requestId) {
        return requestService.getItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithResponseDto> getAllItemRequests(@RequestHeader(value = Headers.SHARER_USER_ID, required = true) Long userId) {
        return requestService.getAllItemRequests(userId);
    }

}
