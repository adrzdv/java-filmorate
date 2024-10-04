package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;

public interface EventStorage {

    void createEvent(long userId, EventType eventType, Operations operation, long entityId) throws NotFoundException;
}
