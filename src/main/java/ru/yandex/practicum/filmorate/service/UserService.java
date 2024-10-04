package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    /**
     * Add a new friend to current user
     *
     * @param idUser   current user's id
     * @param idFriend friend's id
     * @throws NotFoundException
     */
    public void addFriend(Long idUser, Long idFriend) throws NotFoundException {

        userStorage.addFriend(idUser, idFriend);
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
     * @throws NotFoundException
     */
    public void deleteFriend(Long idUser, Long idFriend) throws NotFoundException {

        userStorage.deleteFriend(idUser, idFriend);
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
     * Get list of existing users
     *
     * @return List of users
     */
    public List<User> getAll() {

        return userStorage.getAll();
    }

    public void deleteUserById(Long id) {

        userStorage.deleteUserById(id);
    }

    public List<Event> getUserEvent(Long userId) {
        userStorage.getUser(userId);
        return userStorage.getUserEvent(userId);
    }
}
