package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }


    /**
     * Add a new film
     *
     * @param film new film object
     * @return Film
     */
    @PostMapping
    public Film addNew(@Valid @RequestBody Film film) {

        return filmStorage.addNew(film);
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
    public Film update(@Valid @RequestBody Film film) throws ConditionsException, NotFoundException {

        return filmStorage.update(film);
    }


    /**
     * Get all existing films
     *
     * @return Collection of Film
     */
    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getAll();

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
                     @PathVariable Long userId) throws NotFoundException, DuplicateException, ConditionsException {

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
                        @PathVariable Long userId) throws ConditionsException, NotFoundException {

        return filmService.deleteLike(id, userId);
    }

    /**
     * Get first the most popular list of film. Default value for return = 10
     *
     * @param count number of films for present
     * @return Collection of films
     */
    @GetMapping(value = "popular")
    public Collection<Film> getMostPopular(@RequestParam(defaultValue = "10") int count) {

        return filmService.getMostPopular(count);
    }

}
