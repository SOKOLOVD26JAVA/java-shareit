package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private int id;
    private String name;
    private String email;
    private List<Item> itemList;
}
