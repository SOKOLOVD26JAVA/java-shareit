package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public ItemDto createItem(Long userId, ItemDto itemDto) {


        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(owner);

        itemRepository.save(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден."));
        userValidation(user, item);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    public ItemDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден."));

        List<Booking> approvedBookings = bookingRepository.findByItemIdAndStatus(itemId, BookingStatus.APPROVED);

        LocalDateTime lastBooking = approvedBookings.stream().filter(booking -> booking.getEnd()
                .isBefore(LocalDateTime.now())).map(Booking::getEnd).max(LocalDateTime::compareTo).orElse(null);
        LocalDateTime nextBooking = approvedBookings.stream().filter((booking -> booking.getStart()
                .isAfter(LocalDateTime.now()))).map(Booking::getStart).max(LocalDateTime::compareTo).orElse(null);

        ItemDto itemDto = ItemMapper.mapToItemDto(item);
        itemDto.setLastBooking(null);// Сейчас постман требует Null в last и next booking.
        itemDto.setNextBooking(null);
        return itemDto;
    }

    public List<ItemForUserDto> searchItem(Long userId, String text) {
        List<Item> items = itemRepository.findItemsByText(text);

        return items.stream().filter(item -> item.getAvailable() == true)
                .map(ItemMapper::mapToItemForUserDto).collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto createComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет не найден."));

        if (existsPastApprovedBooking(user.getId(), item.getId())) {
            Comment comment = new Comment();
            comment.setText(commentRequestDto.getText());
            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreated(LocalDateTime.now());

            return CommentMapper.mapToCommentResponseDto(commentRepository.save(comment));
        } else {
            throw new BookingValidationException("Пользователь " + user.getName() + " не брал в аренду вещь " + item.getName());
        }

    }

    public List<ItemForUserDto> getItemListByUserId(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        return itemRepository.findByOwnerId(userId).stream().map(ItemMapper::mapToItemForUserDto).collect(Collectors.toList());
    }

    private boolean existsPastApprovedBooking(Long userId, Long itemId) {
        return commentRepository.existsPastApprovedBooking(userId, itemId);
    }

    private void userValidation(User user, Item item) {
        if (!Objects.equals(item.getOwner().getId(), user.getId())) {
            throw new ValidationException("Ошибка валидации пользователя.");
        } else {
            return;
        }
    }
}
