package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> userMap = new HashMap<>();

    @Override
    public User addNew(User user) {
        Optional<Long> idOptional = getId();
        Long id;
        if (idOptional.isPresent()) {
            id = idOptional.get();
            id++;
        } else {
            id = 1L;
        }
        user.setId(id);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setFriends(new TreeSet<Long>());
        userMap.put(user.getId(), user);
        log.info("New user: {} added by id: {}", user.getLogin(), user.getId());
        return user;
    }

    @Override
    public User update(User user) throws NotFoundException, ConditionsException {
        if (user.getId() == null) {
            log.info("Requested user's id is null");
            throw new ConditionsException("Id cant be NULL");
        }

        if (userMap.containsKey(user.getId())) {
            if (user.getFriends() == null) {
                user.setFriends(new TreeSet<>());
            }

            userMap.put(user.getId(), user);
            log.info("User id: {} login: {} updated", user.getId(), user.getLogin());
        } else {
            log.warn("User id: {} login: {} not found", user.getId(), user.getLogin());
            throw new NotFoundException("User not found");
        }

        return userMap.get(user.getId());
    }

    @Override
    public Collection<User> getAll() {

        return userMap.values();
    }

    @Override
    public User getUser(Long id) throws NotFoundException {

        if (!userMap.containsKey(id)) {
            log.warn("User id: {} not found", id);
            throw new NotFoundException("User not found");
        }

        return userMap.get(id);

    }

    @Override
    public List<User> addFriend(Long idUser, Long idFriend) throws NotFoundException, DuplicateException {

        if (!userMap.containsKey(idUser)) {
            log.warn("User id: {} not found", idUser);
            throw new NotFoundException("User not found");
        } else if (!userMap.containsKey(idFriend)) {
            log.warn("Friend id: {} not found", idFriend);
            throw new NotFoundException("User not found");
        }

        User user = userMap.get(idUser);
        User friend = userMap.get(idFriend);

        Set<Long> userFriends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();


        if (userFriends.contains(idFriend) || friendFriends.contains(idUser)) {
            log.warn("Users already had added to both friend list");
            throw new DuplicateException("Already added to friend list");
        }

        userFriends.add(idFriend);
        friendFriends.add(idUser);
        user.setFriends(userFriends);
        friend.setFriends(friendFriends);
        userMap.put(user.getId(), user);
        log.info("Friend id: {} added to user's friend list userId: {}", idFriend, idUser);
        userMap.put(friend.getId(), friend);
        log.info("Friend id: {} added to user's friend list userId: {}", idUser, idFriend);

        return List.of(user, friend);

    }

    @Override
    public List<User> deleteFriend(Long idUser, Long idFriend) throws NotFoundException {

        if (!userMap.containsKey(idUser) || !userMap.containsKey(idFriend)) {
            log.warn("User id: {} not found", idUser);
            throw new NotFoundException("User not found");
        }

        User user = userMap.get(idUser);
        User friend = userMap.get(idFriend);

        Set<Long> userFriends = user.getFriends();
        Set<Long> friendFriends = friend.getFriends();

        userFriends.remove(friend.getId());
        log.info("Friend id: {} removed from user's friend list userId: {}", idFriend, idUser);
        friendFriends.remove(user.getId());
        log.info("Friend id: {} removed from user's friend list userId: {}", idUser, idFriend);

        user.setFriends(userFriends);
        friend.setFriends(friendFriends);

        userMap.put(user.getId(), user);
        userMap.put(friend.getId(), friend);

        return List.of(user, friend);
    }

    @Override
    public Collection<User> getCommon(Long id, Long otherId) throws NoCommonUsers {
        User user = userMap.get(id);
        User otherUser = userMap.get(otherId);


        Collection<User> commonUsers = userMap.values().stream()
                .filter(u -> u.getFriends().contains(user.getId()) &&
                        u.getFriends().contains(otherUser.getId()))
                .collect(Collectors.toList());
        if (commonUsers.isEmpty()) {
            log.warn("Users user1: {} and user2: {} have no common friends", id, otherId);
            throw new NoCommonUsers("No common users");
        }

        return commonUsers;
    }

    @Override
    public Collection<User> getFriends(Long id) throws NotFoundException {

        if (!userMap.containsKey(id)) {
            log.warn("User id: {} not found", id);
            throw new NotFoundException("User not found");
        }

        User user = userMap.get(id);

        Collection<User> userCollection = userMap.values();

        return userCollection.stream()
                .filter(u -> !u.getFriends().isEmpty())
                .filter(u -> u.getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }

    private Optional<Long> getId() {

        return userMap.keySet().stream()
                .max(Long::compare);
    }

}
