package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;


    /**
     * Add a new friend to current user
     *
     * @param idUser   current user's id
     * @param idFriend friend's id
     * @return int (1 - if method execute success)
     * @throws NotFoundException
     */
    public int addFriend(Long idUser, Long idFriend) throws NotFoundException {

        return userStorage.addFriend(idUser, idFriend);

    }

    /**
     * Get current user
     *
     * @param id User's id
     * @return User
     */
    public User getUser(Long id) {

        return userStorage.getUser(id);
    }

    /**
     * Delete friend in current user's friend list
     *
     * @param idUser   current user's id
     * @param idFriend friend's id
     * @return int (1 - if method execute success)
     * @throws NotFoundException
     */
    public int deleteFriend(Long idUser, Long idFriend) throws NotFoundException {

        return userStorage.deleteFriend(idUser, idFriend);

    }

    /**
     * Get all friends of current user
     *
     * @param id current user's id
     * @return List of users
     * @throws NotFoundException
     */
    public List<User> getFriends(Long id) throws NotFoundException {

        return userStorage.getFriends(id);

    }

    /**
     * Get common users
     *
     * @param id      first user's id
     * @param otherId another user's id
     * @return List of users
     */
    public List<User> getCommon(Long id, Long otherId) {

        return userStorage.getCommon(id, otherId);

    }

    /**
     * Add a new user object
     *
     * @param user new user object for add
     * @return User
     */
    public User addNew(User user) {

        return userStorage.addNew(user);
    }

    /**
     * Update an existing user
     *
     * @param user user object for update
     * @return User
     * @throws NotFoundException
     */
    public User update(User user) throws NotFoundException {

        return userStorage.update(user);
    }

    /**
     * Get collection of existing users
     *
     * @return List of users
     */
    public List<User> getAll() {

        return userStorage.getAll();
    }
}
