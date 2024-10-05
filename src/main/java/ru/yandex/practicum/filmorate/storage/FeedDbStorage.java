package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class FeedDbStorage implements FeedStorage {
    private final JdbcTemplate jdbc;
    private final EventMapper eventMapper;
    private final UserStorage userStorage;

    /**
     * Method to create a record in the feed
     *
     * @param userId    identification number of user
     * @param eventType type of event
     * @param operation type of operation
     * @param entityId  identification number of entity
     * @throws NotFoundException
     */
    @Override
    public void createEvent(long userId, EventType eventType, Operations operation, long entityId) throws NotFoundException {

        if (userId < 0) {
            throw new NotFoundException("Incorrect user's id");
        }
        String query = "INSERT INTO FEED (TIMESTAMP, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(query, Timestamp.from(Instant.now()).getTime(), userId,
                eventType.name(), operation.name(), entityId);

    }

    /**
     * Method for get the feed
     *
     * @param userId user's id
     * @return List of Event
     * @throws NotFoundException
     */
    @Override
    public List<Event> getFeed(Long userId) throws NotFoundException {
        if (!userChecker(userId)) {
            throw new NotFoundException("User not found");
        }
        String query = "SELECT DISTINCT * FROM FEED WHERE USER_ID = ?";
        return jdbc.query(query, eventMapper, userId);
    }


    /**
     * Service method for check user for existing
     *
     * @param userId identification number
     * @return boolean
     */
    private boolean userChecker(Long userId) {
        User user = userStorage.getUser(userId);
        if (user != null) {
            return true;
        }
        return false;
    }
}
