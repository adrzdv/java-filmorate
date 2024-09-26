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

import java.util.List;

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
    public Film update(@Valid @RequestBody Film film) throws NotFoundException {

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
                     @PathVariable Long userId) throws NotFoundException, DuplicateException {

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
                        @PathVariable Long userId) {

        return filmService.deleteLike(id, userId);
    }

    /**
     * Get first the most popular list of film.
     * Default value for return = 10
     *
     * @param count number of films for present
     * @return List of films
     */
    @GetMapping(value = "popular")
    public List<Film> getMostPopular(@RequestParam(defaultValue = "10") int count) {

        return filmService.getMostPopular(count);
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
     * @param filmId    film's id
     */
    @DeleteMapping(value = {"/{filmId}"})
    public void deleteFilmById(@PathVariable Long filmId) {

        filmService.deleteFilmById(filmId);
    }

}
