package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /**
     * Add a like to film
     *
     * @param id     film's id
     * @param userId user's id
     * @return Film
     * @throws NotFoundException
     * @throws DuplicateException
     */
    public Film addLike(Long id, Long userId) throws NotFoundException, DuplicateException {

        return filmStorage.addLike(id, userId, userStorage);
    }

    /**
     * Remove user's like
     *
     * @param id     film's id
     * @param userId user's id
     * @return Film
     * @throws NotFoundException
     */

    public Film deleteLike(Long id, Long userId) throws NotFoundException {

        return filmStorage.removeLike(id, userId, userStorage);

    }

    /**
     * Returns list of the most rated films
     *
     * @param count size of returning list
     * @return Collection of film
     */

    public Collection<Film> getMostPopular(int count) {

        return filmStorage.getMostRated(count);

    }

    /**
     * Add a new film object
     *
     * @param film current film object to add
     * @return Film
     */

    public Film addNew(Film film) {

        return filmStorage.addNew(film);
    }

    /**
     * Update an existing film
     *
     * @param film film object for update
     * @return Film
     * @throws ConditionsException
     * @throws NotFoundException
     */

    public Film update(Film film) throws ConditionsException, NotFoundException {

        return filmStorage.update(film);
    }

    /**
     * Get collection of all existing films
     *
     * @return Collection of film
     */
    public Collection<Film> getAll() {

        return filmStorage.getAll();
    }

}
