package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    Optional<User> getUser(int userId);

    User update(User user);

    void delete(User user);

    List<User> findAll();
}