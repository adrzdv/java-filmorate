package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {

        this.filmService = filmService;
    }


    /**
     * Add a new film
     *
     * @param film new film object
     * @return Film
     * @throws NotFoundException
     * @throws BadRequest
     */
    @PostMapping
    public Film addNew(@Valid @RequestBody Film film) throws NotFoundException, BadRequest {

        return filmService.addNew(film);

    }

    /**
     * Get a film by id
     *
     * @param id film's id
     * @return Film object
     * @throws NotFoundException
     */
    @GetMapping("/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable Long id) throws NotFoundException {
        return filmService.getFilm(id);
    }

    /**
     * Update existing current film
     *
     * @param film current film for update
     * @return Film
     * @throws ConditionsException
     * @throws NotFoundException
     */
    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws NotFoundException, BadRequest {

        return filmService.update(film);
    }


    /**
     * Get all existing films
     *
     * @return List of Film
     */
    @GetMapping
    public List<Film> getAll() {

        return filmService.getAll();

    }

    /**
     * Rating current film by user
     *
     * @param id     film's id
     * @param userId user's if
     * @return Film
     */
    @PutMapping(value = "/{id}/like/{userId}")
    public Film like(@PathVariable Long id,
                     @PathVariable Long userId) throws NotFoundException, DuplicateException, BadRequest {

        return filmService.addLike(id, userId);
    }

    /**
     * Remove user's like
     *
     * @param id     film's id
     * @param userId user's id
     * @return Film
     */
    @DeleteMapping(value = "/{id}/like/{userId}")
    public Film dislike(@PathVariable Long id,
                        @PathVariable Long userId) throws NotFoundException, BadRequest {

        return filmService.deleteLike(id, userId);
    }

    /**
     * Get first the most popular list of film by genre and year
     * Default value for return = 10
     *
     * @param count   number of films for present
     * @param genreId genre's id
     * @param year    year of film's release_date
     * @return List of films
     */

    @GetMapping(value = "popular")
    public List<Film> getMostPopular(@RequestParam(defaultValue = "10") int count,
                                     @RequestParam(required = false) Integer genreId,
                                     @RequestParam(required = false) Integer year) {

        return filmService.getMostPopular(count, genreId, year);
    }

    /**
     * Search by film title or director
     *
     * @param query query for search
     * @param by    parameters for search: can be director,
     * @return List of films
     */
    @GetMapping(value = "/search")
    public List<Film> search(@RequestParam String query,
                             @RequestParam String by) {

        return filmService.search(query, by);
    }


    /**
     * @param id
     * @param sortBy parameter for determining sorting type: year - for sorting by release date,
     *               likes - sorting by likes count
     * @return
     */
    @GetMapping(value = "/director/{id}")
    public List<Film> getByDirector(@PathVariable int id,
                                    @RequestParam String sortBy) throws NotFoundException {
        return filmService.getByDirector(id, sortBy);
    }

    @GetMapping("/common")
    public ResponseEntity<List<Film>> getCommonFilms(@RequestParam Long userId,
                                                     @RequestParam Long friendId) throws BadRequest {
        log.info("Запрос на получение общих фильмов для userId: {}, friendId: {}", userId, friendId);

        List<Film> commonFilms = filmService.getCommonFilms(userId, friendId);

        return ResponseEntity.ok(commonFilms);
    }


    /**
     * Remove film by ID
     *
     * @param filmId film's id
     */
    @DeleteMapping(value = {"/{filmId}"})
    public void deleteFilmById(@PathVariable Long filmId) {

        filmService.deleteFilmById(filmId);

    }

}
