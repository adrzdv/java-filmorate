package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    /**
     * Add new user
     *
     * @param user new user object
     * @return User object
     */
    User addNew(User user);

    /**
     * Update current user
     *
     * @param user current user object
     * @return User object
     * @throws NotFoundException
     * @throws ConditionsException
     */
    User update(User user) throws NotFoundException, ConditionsException;

    /**
     * Get list of user
     *
     * @return Collection of users
     */
    Collection<User> getAll();

    /**
     * Get current user by id
     *
     * @param id current user's id
     * @return Optional value of user
     */
    User getUser(Long id) throws NotFoundException;

}
