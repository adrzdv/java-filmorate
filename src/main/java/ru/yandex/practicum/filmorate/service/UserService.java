package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    @Getter
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;

    }

    /**
     * Add a new friend to current user
     *
     * @param idUser   current user's id
     * @param idFriend friend's id
     * @return List of users
     * @throws NotFoundException
     * @throws DuplicateException
     */
    public List<User> addFriend(Long idUser, Long idFriend) throws NotFoundException,
            DuplicateException {

        return userStorage.addFriend(idUser, idFriend);

    }

    /**
     * Delete friend in current user's friend list
     *
     * @param idUser   current user's id
     * @param idFriend friend's id
     * @return List of users
     * @throws NotFoundException
     */
    public List<User> deleteFriend(Long idUser, Long idFriend) throws NotFoundException {

        return userStorage.deleteFriend(idUser, idFriend);

    }

    /**
     * Get all friends of current user
     *
     * @param id current user's id
     * @return Collection of users
     */
    public Collection<User> getFriends(Long id) throws NotFoundException {

        return userStorage.getFriends(id);

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
    public Collection<User> getCommon(Long id, Long otherId) throws NotFoundException, NoCommonUsers {

        return userStorage.getCommon(id, otherId);

    }
}
