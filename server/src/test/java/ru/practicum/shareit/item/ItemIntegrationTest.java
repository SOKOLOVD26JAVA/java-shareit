package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForUserDto;

import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ItemIntegrationTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserRepository userRepository;

    private User userForTest;
    private ItemDto request;

    @BeforeEach
    void setUpUser() {
        userRepository.deleteAll();

        userForTest = new User();
        userForTest.setName("Юзер");
        userForTest.setEmail("email@yandex.ru");
        userForTest = userRepository.save(userForTest);
    }

    @BeforeEach
    void setUpItem() {
        request = new ItemDto();
        request.setName("молоток");
        request.setDescription("красивый");
        request.setAvailable(true);
    }

    @Test
    void createUserTest() {
        ItemDto savedItem = itemService.createItem(userForTest.getId(), request);

        assertThat(savedItem.getId(), notNullValue());
        assertThat(savedItem.getName(), equalTo(request.getName()));
        assertThat(savedItem.getDescription(), equalTo(request.getDescription()));
        assertThat(savedItem.getAvailable(), equalTo(request.getAvailable()));
    }

    @Test
    void updateItemTest() {
        ItemDto savedItem = itemService.createItem(userForTest.getId(), request);
        request.setName("Не молоток");
        request.setDescription("Не красивый");
        ItemDto dto = itemService.updateItem(userForTest.getId(), savedItem.getId(), request);

        assertThat(dto.getId(), equalTo(savedItem.getId()));
        assertThat(dto.getName(), equalTo("Не молоток"));
        assertThat(dto.getDescription(), equalTo("Не красивый"));
    }

    @Test
    void searchItemTest() {
        itemService.createItem(userForTest.getId(), request);

        List<ItemForUserDto> items = itemService.searchItem(userForTest.getId(), "молоток");
        List<ItemForUserDto> emptyItemList = itemService.searchItem(userForTest.getId(), "не молоток");

        assertThat(items.size(), equalTo(1));
        assertThat(items.getFirst().getName(), equalTo("молоток"));

        assertThat(emptyItemList.size(), equalTo(0));

    }




}
