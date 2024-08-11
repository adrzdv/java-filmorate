package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsException;
import ru.yandex.practicum.filmorate.exceptions.DuplicateException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

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
            log.warn("Film id: {} not found", id);
            throw new NotFoundException("Film not found");
        }

        return filmMap.get(id);

    }

    @Override
    public Film addLike(Long id, Long userId, UserStorage userStorage) throws DuplicateException, NotFoundException {

        if (!filmMap.containsKey(id)) {
            log.warn("Film id: {} not found", id);
            throw new NotFoundException("Film not found");
        }

        Film film = filmMap.get(id);

        Set<Long> likes = film.getLikes();

        if (userStorage.getUser(userId) == null) {
            log.warn("User id: {} not found", userId);
            throw new NotFoundException("User not found");
        }

        if (likes.contains(userId)) {
            log.info("Film id: {} has already rated by User id: {}", id, userId);
            throw new DuplicateException("Current user already had liked");
        }

        likes.add(userId);
        film.setLikesCount(film.getLikesCount() + 1);
        film.setLikes(likes);
        log.info("User id: {} rated film id: {}", userId, id);
        filmMap.put(film.getId(), film);

        return film;
    }

    @Override
    public Film removeLike(Long id, Long userId, UserStorage userStorage) throws NotFoundException {

        if (!filmMap.containsKey(id)) {
            log.warn("Film id: {} not found", id);
            throw new NotFoundException("Film not found");
        }

        Film film = filmMap.get(id);

        Set<Long> likes = film.getLikes();

        if (!likes.contains(userId)) {
            log.warn("Film id: {} hasn't rated by User: {}", id, userId);
            throw new NotFoundException("Like-list doesn't contain current user's id");
        }

        likes.remove(userId);
        film.setLikes(likes);
        film.setLikesCount(film.getLikesCount() - 1);
        log.info("User id: {} disliked film id: {}", userId, id);
        filmMap.put(film.getId(), film);

        return film;
    }

    @Override
    public Collection<Film> getMostRated(int count) {

        return filmMap.values().stream()
                .filter(f -> f.getLikes() != null)
                .filter(f -> f.getLikesCount() > 0)
                .sorted(Comparator.comparing(Film::getLikesCount).reversed())
                .limit(count)
                .collect(Collectors.toList());

    }

    private Optional<Long> getId() {

        return filmMap.keySet().stream()
                .max(Long::compare);
    }
}
