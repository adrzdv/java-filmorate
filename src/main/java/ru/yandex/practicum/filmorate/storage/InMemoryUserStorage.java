package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> userMap = new HashMap<>();

    @Override
    public User addNew(User user) {
        Optional<Long> idOptional = getId();
        Long id;
        if (idOptional.isPresent()) {
            id = idOptional.get();
            id++;
        } else {
            id = 1L;
        }
        user.setId(id);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setFriends(new TreeSet<Long>());
        userMap.put(user.getId(), user);
        log.info("New user: {} added by id: {}", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User update(User user) throws NotFoundException, ConditionsException {
        if (user.getId() == null) {
            log.info("Requested user's id is null");
            throw new ConditionsException("Id cant be NULL");
        }

        if (userMap.containsKey(user.getId())) {
            if (user.getFriends() == null) {
                user.setFriends(new TreeSet<>());
            }

            userMap.put(user.getId(), user);
            log.info("User id: {} login: {} updated", user.getId(), user.getLogin());
        } else {
            log.warn("User id: {} login: {} not found", user.getId(), user.getLogin());
            throw new NotFoundException("User not found");
        }

        return userMap.get(user.getId());
    }

    @Override
    public Collection<User> getAll() {

        return userMap.values();
    }

    @Override
    public User getUser(Long id) throws NotFoundException {

        if (!userMap.containsKey(id)) {
            throw new NotFoundException("User not found");
        }

        return userMap.get(id);

    }

    private Optional<Long> getId() {

        return userMap.keySet().stream()
                .max(Long::compare);
    }

}
