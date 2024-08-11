package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

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
     * @throws NotFoundException
     */
    User getUser(Long id) throws NotFoundException;

    /**
     * Get common users
     *
     * @param id      first user's id
     * @param otherId other user's id
     * @return Collection of users
     * @throws NotFoundException
     * @throws NoCommonUsers
     */
    Collection<User> getCommon(Long id, Long otherId) throws NotFoundException, NoCommonUsers;

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
     * @return List of users
     * @throws NotFoundException
     * @throws DuplicateException
     */
    List<User> addFriend(Long idUser, Long idFriend) throws NotFoundException, DuplicateException;

    /**
     * Delete user's friend
     *
     * @param idUser   user's id
     * @param idFriend friend's id to remove
     * @return List of users
     * @throws NotFoundException
     */
    List<User> deleteFriend(Long idUser, Long idFriend) throws NotFoundException;

}
