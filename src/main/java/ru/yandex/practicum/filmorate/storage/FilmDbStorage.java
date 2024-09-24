package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NoCommonFilmsException;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@AllArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbc;
    private final FilmMapper filmMapper;
    private final MpaMapper mpaMapper;
    private final FilmResultExtractor filmResultExtractor;
    private final FilmRatedMapper filmRatedMapper;

    @Override
    public Film addNew(Film filmAdd) throws BadRequest {

        String query = "INSERT INTO FILMS (TITLE, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATE) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String finalQuery = query;
        Film film;

        if (filmAdd.getGenres() != null) {
            film = deleteDuplicates(filmAdd);
        } else {
            film = filmAdd;
        }

        try {
            String queryMpa = "SELECT * FROM MPA WHERE ID = ?";
            MpaRating mpa = jdbc.queryForObject(queryMpa, mpaMapper, film.getMpa().getId());
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequest("Some troubles");
        }

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(finalQuery, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setString(3, film.getReleaseDate().toString());
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();

        if (filmAdd.getGenres() != null) {
            List<Genre> filmGenre = film.getGenres();
            for (Genre genre : filmGenre) {
                query = "INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) " +
                        "VALUES (?, ?)";
                try {
                    jdbc.update(query, key, genre.getId());
                } catch (DataIntegrityViolationException e) {
                    throw new BadRequest("Some troubles");
                }
            }
        }

        return getFilm(key);
    }

    @Override
    public Film update(Film film) {

        String queryFilm = "UPDATE FILMS SET ID = ?, TITLE = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "DURATION = ?, MPA_RATE = ? WHERE ID = ?";
        String queryGenres = "UPDATE FILMS_GENRE SET GENRE_ID = ? WHERE FILM_ID = ?";

        jdbc.update(queryFilm, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (film.getGenres() != null) {
            List<Genre> filmGenres = film.getGenres();
            for (Genre genre : filmGenres) {
                jdbc.update(queryGenres, genre.getId(), film.getId());
            }
        }

        return getFilm(film.getId());
    }

    @Override
    public List<Film> getAll() {

        String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, " +
                "MPA.ID AS MPA_ID, MPA.NAME AS MPA_RATE, GENRE.ID AS GENRE_ID, GENRE.NAME AS GENRE FROM FILMS " +
                "LEFT JOIN MPA ON FILMS.MPA_RATE = MPA.ID " +
                "LEFT JOIN FILMS_GENRE ON FILMS.ID = FILMS_GENRE.FILM_ID " +
                "LEFT JOIN GENRE ON GENRE.ID = FILMS_GENRE.GENRE_ID ORDER BY FILMS.ID";

        return jdbc.query(query, filmResultExtractor);
    }

    @Override
    public Film getFilm(Long id) {

        String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, " +
                "MPA.ID AS MPA_ID, MPA.NAME AS MPA_RATE, GENRE.ID AS GENRE_ID, GENRE.NAME AS GENRE FROM FILMS " +
                "LEFT JOIN MPA ON FILMS.MPA_RATE = MPA.ID " +
                "LEFT JOIN FILMS_GENRE ON FILMS.ID = FILMS_GENRE.FILM_ID " +
                "LEFT JOIN GENRE ON GENRE.ID = FILMS_GENRE.GENRE_ID WHERE FILMS.ID = ?" +
                "ORDER BY FILMS.ID";

        return jdbc.queryForObject(query, filmMapper, id);

    }

    @Override
    public Film addLike(Long id, Long userId) {

        String query = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbc.update(query, id, userId);

        return getFilm(id);
    }

    @Override
    public List<Film> getMostRated(int count) {

        String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, " +
                "FILMS.MPA_RATE AS MPA_ID, MPA.NAME AS MPA_RATE, COUNT(LIKES.USER_ID) AS LIKES " +
                "FROM FILMS " +
                "LEFT JOIN MPA ON MPA.ID = FILMS.MPA_RATE " +
                "LEFT JOIN FILMS_GENRE ON FILMS_GENRE.FILM_ID = FILMS.ID " +
                "LEFT JOIN LIKES ON FILMS.ID = LIKES.FILM_ID " +
                "GROUP BY FILMS.ID " +
                "ORDER BY likes DESC LIMIT ?";

        return jdbc.query(query, filmRatedMapper, count);
    }

    @Override
    public Film removeLike(Long id, Long userId) {

        String query = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbc.update(query, id, userId);
        return getFilm(id);
    }

    /**
     * Remove duplicate film genres
     *
     * @param film
     * @return Film
     * @throws BadRequest
     *
     */
    private Film deleteDuplicates(Film film) throws BadRequest {

        if (film.getGenres() == null) {
            throw new BadRequest("Some troubles");
        }
        List<Genre> genreList = film.getGenres();
        List<Genre> newGenreList = new ArrayList<>();
        Film newFilm = film;
        for (Genre genre : genreList) {
            if (!newGenreList.contains(genre)) {
                newGenreList.add(genre);
            }
        }
        newFilm.setGenres(newGenreList);

        return newFilm;
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, " +
                "MPA.ID AS MPA_ID, MPA.NAME AS MPA_RATE, COUNT(LIKES.USER_ID) AS LIKES " +
                "FROM FILMS " +
                "JOIN LIKES ON FILMS.ID = LIKES.FILM_ID " +
                "WHERE LIKES.USER_ID IN (?, ?) " +
                "GROUP BY FILMS.ID " +
                "HAVING COUNT(DISTINCT LIKES.USER_ID) = 2 " +
                "ORDER BY LIKES DESC";

        List<Film> commonFilms = jdbc.query(query, filmRatedMapper, userId, friendId);

        if (commonFilms.isEmpty()) {
            throw new NoCommonFilmsException("No common films");
        }
        return commonFilms;
    }
}
