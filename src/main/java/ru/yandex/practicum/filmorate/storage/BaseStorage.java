package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.util.*;

/**
 * Base storage for MpaStorage and GenreStorage
 *
 * @param <T>
 */
public interface BaseStorage<T> {

    /**
     * Get all objects
     *
     * @return List of Mpa or Genre objects
     */
    public List<T> getAll();


    /**
     * Get a one object of Mpa or Genre
     *
     * @param id
     * @return Object
     * @throws NotFoundException
     */
    public Object getOne(int id) throws NotFoundException;
}
