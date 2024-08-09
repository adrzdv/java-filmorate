package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.exceptions.NoCommonUsers;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
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
     * @throws ConditionsException
     * @throws NotFoundException
     * @throws DuplicateException
     */
    public List<User> addFriend(Long idUser, Long idFriend) throws ConditionsException, NotFoundException, DuplicateException {

        User user = userStorage.getUser(idUser);
        User friend = userStorage.getUser(idFriend);

        Set<Long> userFriends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();


        if (userFriends.contains(idFriend) || friendFriends.contains(idUser)) {
            throw new DuplicateException("Already added to friend list");
        }

        userFriends.add(idFriend);
        friendFriends.add(idUser);
        user.setFriends(userFriends);
        friend.setFriends(friendFriends);
        userStorage.update(user);
        userStorage.update(friend);

        return List.of(user, friend);

    }

    /**
     * Delete friend in current user's friend list
     *
     * @param idUser   current user's id
     * @param idFriend friend's id
     * @return List of users
     * @throws NotFoundException
     * @throws ConditionsException
     */
    public List<User> deleteFriend(Long idUser, Long idFriend) throws NotFoundException, ConditionsException {

        User user = userStorage.getUser(idUser);
        User friend = userStorage.getUser(idFriend);

        Set<Long> userFriends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();

        userFriends.remove(friend.getId());
        friendFriends.remove(user.getId());

        user.setFriends(userFriends);
        friend.setFriends(friendFriends);

        userStorage.update(user);
        userStorage.update(friend);

        return List.of(user, friend);

    }

    /**
     * Get all friends of current user
     *
     * @param id current user's id
     * @return Collection of users
     * @throws NotFoundException
     */
    public Collection<User> getFriends(Long id) throws NotFoundException {

        User user = userStorage.getUser(id);

        Collection<User> userCollection = userStorage.getAll();

        Collection<User> userFriends = userCollection.stream()
                .filter(u -> !u.getFriends().isEmpty())
                .filter(u -> u.getFriends().contains(user.getId()))
                .collect(Collectors.toList());

        return userFriends;

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

        User user = userStorage.getUser(id);
        User otherUser = userStorage.getUser(otherId);

        if (user.getFriends().isEmpty() || otherUser.getFriends().isEmpty()) {
            throw new NotFoundException("User have no friends");
        }

        Collection<User> commonUsers = userStorage.getAll().stream()
                .filter(u -> u.getFriends().contains(user.getId()) &&
                        u.getFriends().contains(otherUser.getId()))
                .collect(Collectors.toList());
        if (commonUsers.isEmpty()) {
            throw new NoCommonUsers("No common users");
        }

        return commonUsers;

    }
}
