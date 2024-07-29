package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {


    /**
     * Add a new film
     */
    private final Map<Long, Film> filmMap = new HashMap<>();

    @PostMapping
    public Film addNew(@Valid @RequestBody Film film) {

        Optional<Long> idOptional = getId();
        Long id;
        if (idOptional.isPresent()) {
            id = idOptional.get();
            id++;
        } else {
            id = 1L;
        }
        film.setId(id);
        filmMap.put(film.getId(), film);
        log.info("New film name: {} added by id: {}", film.getName(), film.getId());

        return film;
    }

    /**
     * Update an existing film
     */

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ConditionsException, NotFoundException {

        if (film.getId() == null) {
            log.info("Requested film id is null");
            throw new ConditionsException("Id cant be NULL");
        }

        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Film id: {} name {} updated", film.getId(), film.getName());
        } else {
            log.warn("Object id {} name {} not found", film.getId(), film.getName());
            throw new NotFoundException("Film not found");
        }

        return filmMap.get(film.getId());
    }

    /**
     * Get all existing films
     */
    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmMap.values();
    }

    private Optional<Long> getId() {

        return filmMap.keySet().stream()
                .max(Long::compare);
    }
}
