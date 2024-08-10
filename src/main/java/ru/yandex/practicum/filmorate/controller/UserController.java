package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
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
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    /**
     * Add a new user
     *
     * @param user new current user object
     * @return User object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addNew(@Valid @RequestBody User user) {

        return userService.getUserStorage().addNew(user);

    }

    /**
     * Update current user
     *
     * @param user current user object
     * @return User object
     * @throws ConditionsException
     * @throws NotFoundException
     */
    @PutMapping
    public User update(@Valid @RequestBody User user) throws ConditionsException, NotFoundException {

        return userService.getUserStorage().update(user);
    }

    /**
     * Get list of all users
     *
     * @return Collection of users
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsers() {

        return userService.getUserStorage().getAll();

    }


    /**
     * Add a new friend to current user
     *
     * @param id       user's id
     * @param friendId friend's id
     * @return Collection of users
     * @throws DuplicateException
     * @throws NotFoundException
     */
    @PutMapping(value = "/{id}/friends/{friendId}")
    public Collection<User> addFriend(@PathVariable Long id,
                                      @PathVariable Long friendId) throws DuplicateException, NotFoundException {
        return userService.addFriend(id, friendId);
    }

    /**
     * Delete friend from current user
     *
     * @param id       user's id
     * @param friendId friend's id
     * @return Collection of users
     * @throws NotFoundException
     */
    @DeleteMapping(value = "/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> deleteFriend(@PathVariable Long id,
                                         @PathVariable Long friendId) throws NotFoundException {
        return userService.deleteFriend(id, friendId);
    }


    /**
     * Get all user's friends
     *
     * @param id current user's id
     * @return Collection of users
     * @throws NotFoundException
     */
    @GetMapping(value = "/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable Long id) throws NotFoundException {
        return userService.getFriends(id);
    }

    /**
     * Get common users
     *
     * @param id      first user's id
     * @param otherId another user's id
     * @return Collection of users
     * @throws NotFoundException
     * @throws NoCommonUsers
     */
    @GetMapping(value = "{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommon(@PathVariable Long id,
                                      @PathVariable Long otherId) throws NotFoundException, NoCommonUsers {
        return userService.getCommon(id, otherId);
    }

}
