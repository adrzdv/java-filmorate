package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;

public interface EventStorage {

    void createEvent(long userId, EventType eventType, Operations operation, long entityId, JdbcTemplate jdbc);
}
