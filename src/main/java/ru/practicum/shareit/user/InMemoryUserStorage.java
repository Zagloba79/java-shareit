package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component("userStorage")
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long userId = 1L;

    @Override
    public User create(User user) {
        user.setId(userId++);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    private Optional<User> getUserOpt(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User getUser(Long id) {
        return getUserOpt(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователя с " + id + " не существует."));
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