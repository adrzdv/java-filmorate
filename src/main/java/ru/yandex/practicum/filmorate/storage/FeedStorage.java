package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;

import java.util.List;

/**
 * Base interface for storage of feed
 */
public interface FeedStorage {
    void createEvent(long userId, EventType eventType, Operations operation, long entityId) throws NotFoundException, BadRequest;

    List<Event> getFeed(Long userId) throws NotFoundException;

}
