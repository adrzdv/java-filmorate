package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    /**
     * Add a new film object
     *
     * @param film film for adding
     * @return Film
     */
    Film addNew(Film film);

    /**
     * Updating an existing film
     *
     * @param film film for update
     * @return Film
     * @throws ConditionsException
     * @throws NotFoundException
     */
    Film update(Film film) throws ConditionsException, NotFoundException;

    /**
     * Get all existing films
     *
     * @return Collection of film
     */
    Collection<Film> getAll();

    /**
     * Get a film by ID
     *
     * @param id film's id
     * @return Film
     * @throws NotFoundException
     */
    Film getFilm(Long id) throws NotFoundException;


}
