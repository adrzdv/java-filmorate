package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;

import java.sql.Timestamp;
import java.time.Instant;

@Repository
@AllArgsConstructor
public class EventDbStorage implements EventStorage {

    private final JdbcTemplate jdbc;

    @Override
    public void createEvent(long userId, EventType eventType, Operations operation, long entityId, JdbcTemplate jdbc) {

        long timestamp = Timestamp.from(Instant.now()).getTime();
        String sqlQuery = "INSERT INTO feeds (user_id, timestamp, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sqlQuery, userId, timestamp, eventType.name(), operation.name(), entityId);
    }
}
