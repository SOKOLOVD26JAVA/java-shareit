package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class ItemStorage {

    private Map<Integer, Item> items = new HashMap<>();
    private int id = 0;

    public Item createItem(int userId, Item item) {
        item.setId(generateId());
        item.setOwnerId(userId);

        items.put(item.getId(), item);

        return item;
    }

    public Item getItem(int itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new NotFoundException("Данный предмет отсутствует.");
        }
    }

    public Item updateItem(Integer userId, int itemId, Item item) {
        Item updatedItem = getItem(itemId);
        userValidate(itemId, userId);
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        items.put(updatedItem.getId(), updatedItem);

        return updatedItem;
    }

    public List<Item> searchItem(Integer userId, String text) {
        List<Item> findItems = new ArrayList<>(items.values());
        return findItems.stream().filter(item -> item.getAvailable() == true).
                filter(item -> item.getName().toLowerCase().
                        contains(text.toLowerCase()) || item.getDescription().toLowerCase().
                        contains(text.toLowerCase())).collect(Collectors.toList());
    }


    private int generateId() {
        return ++id;
    }

    private void userValidate(int itemId, int userId) {
        Item item = getItem(itemId);

        if (item.getOwnerId() != userId) {
            throw new ValidationException("Ошибка валидации пользователя.");
        } else {
            return;
        }
    }

}
