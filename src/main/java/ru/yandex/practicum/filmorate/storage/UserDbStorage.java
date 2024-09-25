package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final UserMapper userMapper;

    @Override
    public User addNew(User user) {

        String query = "INSERT INTO USERS (NAME, LOGIN, EMAIL, BIRTHDATE) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        long key;

        if (user.getName().isEmpty()) {
            key = insertNewUser(keyHolder, query, user.getLogin(), user.getLogin(), user.getEmail(), user.getBirthday());
        } else {
            key = insertNewUser(keyHolder, query, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday());
        }

        return getUser(key);
    }


    @Override
    public User update(User user) throws NotFoundException {

        String query = "UPDATE USERS SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDATE = ? WHERE ID = ?";
        int res = jdbc.update(query, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        if (res != 1) {
            throw new NotFoundException("User not found");
        }
        return getUser(user.getId());
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM USERS";
        return jdbc.query(query, userMapper);
    }

    @Override
    public User getUser(Long id) {

        String query = "SELECT * FROM USERS WHERE ID = ?";
        return jdbc.queryForObject(query, userMapper, id);
    }

    @Override
    public List<User> getCommon(Long id, Long otherId) {

        String query = " SELECT id, name, email, login, name, birthdate FROM USERS\n" +
                "WHERE id IN (\n" +
                "SELECT friend_id AS user_id\n" +
                "FROM friends\n" +
                "WHERE user_id = ? AND status = 'APPROVED')\n" +
                "AND id IN (SELECT friend_id AS user_id\n" +
                "FROM friends\n" +
                "WHERE user_id = ? AND status = 'APPROVED')";

        return jdbc.query(query, userMapper, id, otherId);
    }

    @Override
    public List<User> getFriends(Long id) throws NotFoundException {

        if (!checkId(id)) {
            throw new NotFoundException("User not found");
        }
        String query = "SELECT ID, NAME, LOGIN, NAME, EMAIL, BIRTHDATE\n" +
                "FROM USERS WHERE id IN\n" +
                "(SELECT friend_id FROM friends where user_id = ?)";
        return jdbc.query(query, userMapper, id);

    }

    @Override
    public void addFriend(Long idUser, Long idFriend) throws NotFoundException {

        if (!checkId(idUser)) {
            throw new NotFoundException("User not found");
        } else if (!checkId(idFriend)) {
            throw new NotFoundException("Friend not found");
        }
        String query = "INSERT INTO FRIENDS (USER_ID, FRIEND_ID, STATUS) VALUES (?, ?, ?)";
        jdbc.update(query, idUser, idFriend, Status.APPROVED.toString());
    }

    @Override
    public void deleteFriend(Long idUser, Long idFriend) throws NotFoundException {

        if (!checkId(idUser)) {
            throw new NotFoundException("User not found");
        } else if (!checkId(idFriend)) {
            throw new NotFoundException("Friend not found");
        }

        String query = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbc.update(query, idUser, idFriend);

    }

    @Override
    public void deleteUserById(Long id) throws EmptyResultDataAccessException {

        String query = "DELETE FROM USERS WHERE ID = ?";
        jdbc.update(query, id);
    }

    /**
     * Add new user in database
     *
     * @param keyHolder
     * @param query
     * @param name
     * @param login
     * @param email
     * @param birthdate
     * @return key of added object
     */
    private long insertNewUser(KeyHolder keyHolder, String query, String name, String login,
                               String email, LocalDate birthdate) {

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, login);
            ps.setString(3, email);
            ps.setString(4, birthdate.toString());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    /**
     * Method for check an id
     *
     * @param id
     * @return boolean
     */
    private boolean checkId(long id) {
        String query = "SELECT NAME FROM USERS WHERE ID = ?";
        try {
            Optional<String> checkString = Optional.ofNullable(jdbc.queryForObject(query, String.class, id));
            return checkString.isPresent();
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
