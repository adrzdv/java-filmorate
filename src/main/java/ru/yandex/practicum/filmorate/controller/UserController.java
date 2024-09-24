package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;


    /**
     * Add a new user
     *
     * @param user new current user object
     * @return User object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addNew(@Valid @RequestBody User user) {

        return userService.addNew(user);

    }

    /**
     * Update current user
     *
     * @param user current user object
     * @return User object
     * @throws NotFoundException
     */
    @PutMapping
    public User update(@Valid @RequestBody User user) throws NotFoundException {

        return userService.update(user);
    }

    /**
     * Get list of all users
     *
     * @return List of users
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {

        return userService.getAll();

    }

    /**
     * Get an existing user by id
     *
     * @param id User's id
     * @return User
     */
    @GetMapping("/{id}")
    @ResponseBody
    public User getUser(@PathVariable Long id) {

        return userService.getUser(id);
    }


    /**
     * Add a new friend to current user
     *
     * @param id       user's id
     * @param friendId friend's id
     * @throws NotFoundException
     */
    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) throws NotFoundException {
        userService.addFriend(id, friendId);
    }

    /**
     * Delete friend from current user
     *
     * @param id       user's id
     * @param friendId friend's id
     * @throws NotFoundException
     */
    @DeleteMapping(value = "/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) throws NotFoundException {
        userService.deleteFriend(id, friendId);
    }

    @DeleteMapping(value = "/{userId}")
    public void removeUserById(@PathVariable Long userId) throws NotFoundException {

        userService.removeUserById(userId);
    }


    /**
     * Get all user's friends
     *
     * @param id current user's id
     * @return List of users
     * @throws NotFoundException
     */
    @GetMapping(value = "/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable Long id) throws NotFoundException {
        return userService.getFriends(id);
    }

    /**
     * Get common users
     *
     * @param id      first user's id
     * @param otherId another user's id
     * @return List of users
     */
    @GetMapping(value = "{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommon(@PathVariable Long id,
                                @PathVariable Long otherId) {
        return userService.getCommon(id, otherId);
    }

}
