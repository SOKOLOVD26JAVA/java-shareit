package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithOutResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ItemRequestIntegrationTest {
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemService itemService;

    private User userForTest;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUpUser() {
        userRepository.deleteAll();

        userForTest = new User();
        userForTest.setName("Юзер");
        userForTest.setEmail("email@yandex.ru");
        userForTest = userRepository.save(userForTest);
    }


    @Test
    void createItemRequestTest() {
        requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужен молоток.");
        ItemRequestWithOutResponseDto createdRequest = itemRequestService.createItemRequest(userForTest.getId(), requestDto);

        assertThat(createdRequest.getId(), notNullValue());
        assertThat(createdRequest.getRequesterId(), equalTo(userForTest.getId()));
    }

    @Test
    void getItemRequestTest() {
        requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужен молоток.");
        ItemRequestWithOutResponseDto createdRequest = itemRequestService.createItemRequest(userForTest.getId(), requestDto);

        ItemRequestWithResponseDto dto = itemRequestService.getItemRequestById(userForTest.getId(), createdRequest.getId());

        assertThat(createdRequest.getId(), notNullValue());
        assertThat(createdRequest.getRequesterId(), equalTo(userForTest.getId()));
    }

    @Test
    void createItemOnRequestTest() {
        requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужен молоток.");
        ItemRequestWithOutResponseDto createdRequest = itemRequestService.createItemRequest(userForTest.getId(), requestDto);

        User anotherUser = new User();
        anotherUser.setName("anotherUser");
        anotherUser.setEmail("another@email.com");
        userRepository.save(anotherUser);

        ItemDto item = new ItemDto();
        item.setName("молоток");
        item.setDescription("красивый");
        item.setAvailable(true);
        item.setRequestId(createdRequest.getId());

        ItemDto createdItemOnRequest = itemService.createItem(anotherUser.getId(),item);

       assertThat(createdItemOnRequest.getRequestId(), equalTo(createdRequest.getId()));
    }
}
