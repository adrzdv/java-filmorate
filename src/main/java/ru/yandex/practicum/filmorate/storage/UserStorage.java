package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

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
     */
    User update(User user) throws NotFoundException;

    /**
     * Get list of user
     *
     * @return List of users
     */
    List<User> getAll();

    /**
     * Get current user by id
     *
     * @param id current user's id
     * @return User object
     */
    User getUser(Long id);

    /**
     * Get common users
     *
     * @param id      first user's id
     * @param otherId other user's id
     * @return List of users
     */
    List<User> getCommon(Long id, Long otherId);

    /**
     * Get user's friend-list
     *
     * @param id user's id
     * @return List of users
     */
    List<User> getFriends(Long id) throws NotFoundException;

    /**
     * Add a new friend to friend list
     *
     * @param idUser   user's id
     * @param idFriend friend's id
     * @throws NotFoundException
     */
    void addFriend(Long idUser, Long idFriend) throws NotFoundException;

    /**
     * Delete user's friend
     *
     * @param idUser   user's id
     * @param idFriend friend's id to remove
     * @throws NotFoundException
     */
    void deleteFriend(Long idUser, Long idFriend) throws NotFoundException;

}
