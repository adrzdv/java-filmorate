package ru.yandex.practicum.filmorate.storage;


import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
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
     */
    Film update(Film film) throws BadRequest;

    /**
     * Get all existing films
     *
     * @return List of film
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
     */
    Film addLike(Long id, Long userId);


    /**
     * Returns list of the most rated films by genre and year
     *
     * @param count   size of returning list
     * @param genreId genre's id
     * @param year    year of film's release_date
     * @return List of film
     */

    List<Film> getMostRated(int count, Integer genreId, Integer year);

    /**
     * Remove a current user's like
     *
     * @param id          film's id
     * @param userId      user's id
     * @return Film
     */
    Film removeLike(Long id, Long userId) throws NotFoundException;

    /**
     * Get film by director
     *
     * @param id    identification number of film director
     * @param param parameter for sorting
     * @return List of film
     */
    List<Film> getByDirector(int id, String param) throws NotFoundException;

     /**
     * Returns a list of common films
     *
     * @param userId   user's id
     * @param friendId user's id
     * @return List of film
     */
    List<Film> getCommonFilms(Long userId, Long friendId);

    /**
     * Get movie recommendations to watch
     *
     * @param id    user`s id
     * @return List of film
     * @throws NotFoundException
     */
    List<Film> getRecommendations(Long id);

     /**
     * Remove film by ID
     *
     * @param id film's id
     * @throws EmptyResultDataAccessException
     */
    void deleteFilmById(Long id) throws EmptyResultDataAccessException;

    /**
     * Search by film title or director
     *
     * @param query  query for search
     * @param params parameters for search
     * @return List of films
     */
    List<Film> search(String query, String params);

}
