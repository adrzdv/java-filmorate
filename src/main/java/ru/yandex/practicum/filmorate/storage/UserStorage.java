package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

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
    User update(User user) throws NotFoundException;

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
     * @throws NotFoundException
     */
    User getUser(Long id);

    /**
     * Get common users
     *
     * @param id      first user's id
     * @param otherId other user's id
     * @return Collection of users
     * @throws NotFoundException
     * @throws NoCommonUsers
     */
    Collection<User> getCommon(Long id, Long otherId);

    /**
     * Get user's friend-list
     *
     * @param id user's id
     * @return Collection of users
     */
    Collection<User> getFriends(Long id) throws NotFoundException;

    /**
     * Add a new user's friend
     *
     * @param idUser   user's id
     * @param idFriend friend's id to add
     * @throws NotFoundException
     * @throws DuplicateException
     */
    int addFriend(Long idUser, Long idFriend) throws NotFoundException;

    /**
     * Delete user's friend
     *
     * @param idUser   user's id
     * @param idFriend friend's id to remove
     * @return List of users
     * @throws NotFoundException
     */
    int deleteFriend(Long idUser, Long idFriend) throws NotFoundException;

}
