package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> filmMap = new HashMap<>();

    @Override
    public Film addNew(Film film) {
        Optional<Long> idOptional = getId();
        Long id;
        if (idOptional.isPresent()) {
            id = idOptional.get();
            id++;
        } else {
            id = 1L;
        }
        film.setId(id);
        film.setLikes(new TreeSet<Long>());
        filmMap.put(film.getId(), film);
        log.info("New film name: {} added by id: {}", film.getName(), film.getId());

        return film;
    }

    @Override
    public Film update(Film film) throws ConditionsException, NotFoundException {
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

    @Override
    public Collection<Film> getAll() {
        return filmMap.values();

    }

    @Override
    public Film getFilm(Long id) throws NotFoundException {

        if (!filmMap.containsKey(id)) {
            throw new NotFoundException("Film not found");
        }

        return filmMap.get(id);

    }

    private Optional<Long> getId() {

        return filmMap.keySet().stream()
                .max(Long::compare);
    }
}
