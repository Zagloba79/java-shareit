package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component("userStorage")
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer userId = 1;

    @Override
    public User create(User user) {
        user.setId(userId++);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public Optional<User> getUser(int userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}