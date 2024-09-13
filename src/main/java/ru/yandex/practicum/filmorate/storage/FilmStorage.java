package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    /**
     * Add a new film object
     *
     * @param film film for adding
     * @return Film
     */
    Film addNew(Film film) throws NotFoundException, BadRequest;

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
    List<Film> getAll();

    /**
     * Get a film by ID
     *
     * @param id film's id
     * @return Film
     * @throws NotFoundException
     */
    Film getFilm(Long id) throws NotFoundException;

    /**
     * Add a like by current user to current film
     *
     * @param id          film's id
     * @param userId      user's id
     * @return Film
     * @throws NotFoundException
     * @throws DuplicateException
     */
    Film addLike(Long id, Long userId);


    /**
     * Returns list of the most rated films
     *
     * @param count size of returning list
     * @return Collection of film
     */
    List<Film> getMostRated(int count);

    /**
     * Remove a current user's like
     *
     * @param id          film's id
     * @param userId      user's id
     * @return Film
     * @throws NotFoundException
     */
    Film removeLike(Long id, Long userId);


}
