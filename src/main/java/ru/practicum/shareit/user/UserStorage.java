package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User getUser(Long userId);

    User update(User user);

    void delete(User user);

    List<User> findAll();
}