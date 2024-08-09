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
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

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
     * @throws ConditionsException
     */
    public Film addLike(Long id, Long userId) throws NotFoundException, DuplicateException, ConditionsException {

        Film film = filmStorage.getFilm(id);

        Set<Long> likes = film.getLikes();

        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("User not found");
        }

        if (likes.contains(userId)) {
            throw new DuplicateException("Current user already had liked");
        }

        likes.add(userId);
        film.setLikesCount(film.getLikesCount() + 1);
        film.setLikes(likes);
        filmStorage.update(film);

        return film;
    }

    /**
     * Remove user's like
     *
     * @param id     film's id
     * @param userId user's id
     * @return Film
     * @throws NotFoundException
     * @throws ConditionsException
     */

    public Film deleteLike(Long id, Long userId) throws NotFoundException, ConditionsException {

        Film film = filmStorage.getFilm(id);

        Set<Long> likes = film.getLikes();

        if (!likes.contains(userId) || likes.isEmpty()) {
            throw new NotFoundException("Like-list doesn't contain current user's id");
        }

        likes.remove(userId);
        film.setLikes(likes);
        film.setLikesCount(film.getLikesCount() - 1);
        filmStorage.update(film);

        return film;

    }

    /**
     * Returns list of the most rated films
     *
     * @param count size of returning list
     * @return Collection of film
     */

    public Collection<Film> getMostPopular(int count) {

        return filmStorage.getAll().stream()
                .filter(f -> f.getLikes() != null)
                .filter(f -> f.getLikesCount() > 0)
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());

    }
}
