package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final FeedStorage feedStorage;


    /**
     * Add a like to film
     *
     * @param id     film's id
     * @param userId user's id
     * @return Film
     */
    public Film addLike(Long id, Long userId) throws NotFoundException, BadRequest {
        feedStorage.createEvent(userId, EventType.LIKE, Operations.ADD, id);
        return filmStorage.addLike(id, userId);
    }

    /**
     * Get an existing film by id
     *
     * @param id
     * @return Film
     * @throws NotFoundException
     */
    public Film getFilm(Long id) throws NotFoundException {

        return filmStorage.getFilm(id);
    }

    /**
     * Remove user's like
     *
     * @param id     film's id
     * @param userId user's id
     * @return Film
     */

    public Film deleteLike(Long id, Long userId) throws NotFoundException, BadRequest {
        feedStorage.createEvent(userId, EventType.LIKE, Operations.REMOVE, id);
        return filmStorage.removeLike(id, userId);

    }

    /**
     * Returns list of the most rated films by genre and year
     *
     * @param count   size of returning list
     * @param genreId genre's id
     * @param year    year of film's release_date
     * @return List of film
     */

    public List<Film> getMostPopular(int count, Integer genreId, Integer year) {

        return filmStorage.getMostRated(count, genreId, year);

    }

    /**
     * Add a new film object
     *
     * @param film current film object to add
     * @return Film
     */

    public Film addNew(Film film) throws NotFoundException, BadRequest {

        return filmStorage.addNew(film);
    }

    /**
     * Update an existing film
     *
     * @param film film object for update
     * @return Film
     * @throws NotFoundException
     */

    public Film update(Film film) throws NotFoundException, BadRequest {

        return filmStorage.update(film);
    }

    /**
     * Get List of all existing films
     *
     * @return List of film
     */
    public List<Film> getAll() {

        return filmStorage.getAll();
    }


    public List<Film> getByDirector(int id, String param) throws NotFoundException {

        return filmStorage.getByDirector(id, param);
    }


    /**
     * Returns a list of common films
     *
     * @param userId   user's id
     * @param friendId user's id
     * @return List of film
     */
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return filmStorage.getCommonFilms(userId, friendId);

    }

    public void deleteFilmById(Long id) {

        filmStorage.deleteFilmById(id);

    }

    /**
     * Search by film title or director
     *
     * @param query  query for search
     * @param params parameters for search
     * @return List of films
     */
    public List<Film> search(String query, String params) {

        return filmStorage.search(query, params);
    }

    /**
     * Get recommendations on movies to watch for the user
     *
     * @param id user`s id
     * @return List of film
     * @throws NotFoundException
     */
    public List<Film> getRecommendations(Long id) {
        return filmStorage.getRecommendations(id);
    }

}
