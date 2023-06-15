package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;

import java.util.*;

@Component("userStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUser(int userId) {
        if (!users.containsKey(userId)) {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new ObjectNotFoundException("Нет такого пользователя");
        }
        return Optional.of(users.get(userId));
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        if (!users.containsKey(id)) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Нет такого пользователя");
        }
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public void delete(User user) {
        int id = user.getId();
        users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
