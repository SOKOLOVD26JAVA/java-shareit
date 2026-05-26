package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithOutResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Transactional
    public ItemRequestWithOutResponseDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto);
        User requester = findUserById(userId);

        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.mapToItemRequestWithOutResponseDto(requestRepository.save(itemRequest));
    }

    public List<ItemRequestWithResponseDto> getUserItemRequests(Long userId) {
        User user = findUserById(userId);
        List<ItemRequest> allItemRequests = requestRepository.findItemRequestsWithItemsByUserID(userId);
        return allItemRequests.stream()
                .map(ItemRequestMapper::mapToItemRequestWithResponseDto).toList();
    }

    public ItemRequestWithResponseDto getItemRequestById(Long userId, Long requestId) {
        User user = findUserById(userId);
        ItemRequestWithResponseDto itemRequest = ItemRequestMapper.mapToItemRequestWithResponseDto(requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Пользователь не найден.")));
        return itemRequest;
    }

    public List<ItemRequestWithResponseDto> getAllItemRequests(Long userId) {
        User user = findUserById(userId);
        return requestRepository.findAll()
                .stream().map(ItemRequestMapper::mapToItemRequestWithResponseDto).sorted(Comparator.comparing(ItemRequestWithResponseDto::getCreated).reversed()).toList();

    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
    }
}
