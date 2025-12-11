package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserStorage {

    private Map<Integer, User> users = new HashMap<>();
    int id = 0;

    public User createUser(User user) {
        emailCheck(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(int id, User user) {
        userContainsCheck(id);
        emailCheck(user);

        User updatedUser = users.get(id);

        updatedUser.setId(id);
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        users.put(id, updatedUser);

        return updatedUser;
    }

    public User getUserByID(int id) {
        userContainsCheck(id);
        return users.get(id);
    }

    public void deleteUserByID(int id) {
        userContainsCheck(id);

        users.remove(id);
    }

    public void addItemToUser(int userId, Item item) {
        User user = getUserByID(userId);
        item.setOwnerId(userId);

        user.getItemList().add(item);
    }

    public List<Item> getUserItemList(int userId) {
        User user = getUserByID(userId);

        return user.getItemList();
    }

    private int generateId() {
        return ++id;
    }

    private void emailCheck(User user) {
        for (User user1 : users.values()) {
            if (user1.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new ValidationException("Пользователь с Email: " + user.getEmail() + " уже зарегистрирован.");
            }
        }
    }

    private void userContainsCheck(int id) {
        if (users.containsKey(id)) {
            return;
        } else {
            throw new NotFoundException("Пользователь с ID: " + id + " не обнаружен.");
        }
    }


}
