package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> userMap = new HashMap<>();

    /**
     * Add a new user
     */
    @PostMapping
    public User addNew(@Valid @RequestBody User user) {
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
        userMap.put(user.getId(), user);
        log.info("New user: {} added by id: {}", user.getLogin(), user.getId());
        return user;
    }

    /**
     * Update an existing user
     */
    @PutMapping
    public User update(@Valid @RequestBody User user) throws ConditionsException, NotFoundException {

        if (user.getId() == null) {
            log.info("Requested user's id is null");
            throw new ConditionsException("Id cant be NULL");
        }

        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("User id: {} login: {} updated", user.getId(), user.getLogin());
        } else {
            log.warn("User id: {} login: {} not found", user.getId(), user.getLogin());
            throw new NotFoundException("User not found");
        }

        return userMap.get(user.getId());
    }


    /**
     * Get all existing users
     */
    @GetMapping
    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    private Optional<Long> getId() {

        return userMap.keySet().stream()
                .max(Long::compare);
    }
}
