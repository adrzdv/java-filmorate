package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.util.*;

/**
 * Base storage for MpaStorage and GenreStorage
 *
 * @param <T>
 */
public interface BaseStorage<T> {

    public List<T> getAll();

    public Optional<T> getOne(int id) throws NotFoundException;
}
