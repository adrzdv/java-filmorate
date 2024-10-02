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
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.FilmRatedMapper;
import ru.yandex.practicum.filmorate.mapper.FilmResultExtractor;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbc;
    private final FilmMapper filmMapper;
    private final MpaMapper mpaMapper;
    private final FilmResultExtractor filmResultExtractor;
    private final UserDbStorage userDbStorage;
    private final FilmRatedMapper filmRatedMapper;
    private final String sqlQuery = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION,\n" +
            "MPA.ID AS MPA_ID, MPA.NAME AS MPA_RATE, GENRE.ID AS GENRE_ID, GENRE.NAME AS GENRE,\n" +
            "DIRECTORS.ID AS DIRECTOR_ID, DIRECTORS.NAME AS DIRECTOR_NAME FROM FILMS\n" +
            "LEFT JOIN MPA ON FILMS.MPA_RATE = MPA.ID\n" +
            "LEFT JOIN FILMS_GENRE ON FILMS.ID = FILMS_GENRE.FILM_ID\n" +
            "LEFT JOIN GENRE ON GENRE.ID = FILMS_GENRE.GENRE_ID\n" +
            "LEFT JOIN FILMS_DIRECTORS ON FILMS_DIRECTORS.FILM_ID = FILMS.ID\n" +
            "LEFT JOIN DIRECTORS ON DIRECTORS.ID = FILMS_DIRECTORS.DIRECTOR_ID\n";

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

        if (filmAdd.getDirectors() != null) {
            List<Director> filmDirectors = film.getDirectors();
            for (Director director : filmDirectors) {
                query = "INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID) " +
                        "VALUES (?, ?)";
                try {
                    jdbc.update(query, key, director.getId());
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
        String queryGenreNotExist = "INSERT INTO FILMS_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
        String queryDir = "UPDATE FILMS_DIRECTORS SET DIRECTOR_ID = ? WHERE FILM_ID = ?";
        String queryDirIfNotExist = "INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";

        List<Director> dirList = getFilm(film.getId()).getDirectors();
        Director filmDirector;
        if (dirList != null) {
            filmDirector = dirList.get(0);
            if (film.getDirectors() != null && filmDirector.getId() != 0) {
                List<Director> filmDirectors = film.getDirectors();
                for (Director director : filmDirectors) {
                    jdbc.update(queryDir, director.getId(), film.getId());
                }
            } else if (film.getDirectors() != null) {
                List<Director> filmDirectors = film.getDirectors();
                for (Director director : filmDirectors) {
                    jdbc.update(queryDirIfNotExist, film.getId(), director.getId());
                }
            }
        }

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

        String query = sqlQuery +
                "ORDER BY FILMS.ID";

        return jdbc.query(query, filmResultExtractor);
    }

    @Override
    public Film getFilm(Long id) {

        String query = sqlQuery +
                "WHERE FILMS.ID = ?" +
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
    public List<Film> getMostRated(int count, Integer genreId, Integer year) {
        StringBuilder query = new StringBuilder("SELECT FILMS.ID, FILMS.TITLE, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, FILMS.DURATION, " +
                "FILMS.MPA_RATE AS MPA_ID, MPA.NAME AS MPA_RATE, COUNT(LIKES.USER_ID) AS LIKES " +
                "FROM FILMS " +
                "LEFT JOIN MPA ON MPA.ID = FILMS.MPA_RATE " +
                "LEFT JOIN FILMS_GENRE ON FILMS_GENRE.FILM_ID = FILMS.ID " +
                "LEFT JOIN LIKES ON FILMS.ID = LIKES.FILM_ID ");

        // Добавление фильтрации по жанру, если указан genreId
        if (genreId != null) {
            query.append("WHERE FILMS_GENRE.GENRE_ID = ").append(genreId).append(" ");
        }

        // Добавление фильтрации по году, если указан year
        if (year != null) {
            if (genreId != null) {
                query.append("AND ");
            } else {
                query.append("WHERE ");
            }
            query.append("YEAR(FILMS.RELEASE_DATE) = ").append(year).append(" ");
        }

        query.append("GROUP BY FILMS.ID " +
                "ORDER BY LIKES DESC LIMIT ?");

        return jdbc.query(query.toString(), filmRatedMapper, count);
    }

    @Override
    public Film removeLike(Long id, Long userId) throws NotFoundException {

        if(!userDbStorage.checkId(userId)) {
            throw new NotFoundException("User not found");
        }
        String query = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbc.update(query, id, userId);

        return getFilm(id);
    }

    @Override
    public List<Film> getByDirector(int id, String param) {

        if (param.equals("year")) {
            String query = sqlQuery +
                    "WHERE DIRECTORS.ID = ?\n" +
                    "ORDER BY FILMS.RELEASE_DATE";
            return jdbc.query(query, filmResultExtractor, id);

        } else if (param.equals("likes")) {
            String query = sqlQuery +
                    "LEFT JOIN (SELECT LIKES.FILM_ID AS id_film, COUNT(LIKES.USER_ID) AS like_count FROM LIKES\n" +
                    "GROUP BY FILM_ID) AS like_count ON like_count.id_film = FILMS.ID\n" +
                    "WHERE DIRECTORS.ID = ?\n" +
                    "ORDER BY like_count.like_count DESC";
            return jdbc.query(query, filmResultExtractor, id);
        }

        return null;
    }

    @Override
    public void deleteFilmById(Long id) throws EmptyResultDataAccessException {

        String query = "DELETE FROM FILMS WHERE ID = ?";
        jdbc.update(query, id);

    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String query = "SELECT f.id, f.title, f.description, f.release_date, f.duration, " +
                "mpa.id AS mpa_id, mpa.name AS mpa_rate, " +
                "genre.id AS genre_id, genre.name AS genre, " +
                "director.id AS director_id, director.name AS director_name " +
                "FROM FILMS f " +
                "LEFT JOIN MPA mpa ON f.mpa_rate = mpa.id " +
                "LEFT JOIN FILMS_GENRE fg ON f.id = fg.film_id " +
                "LEFT JOIN GENRE genre ON fg.genre_id = genre.id " +
                "LEFT JOIN FILMS_DIRECTORS fd ON f.id = fd.film_id " +
                "LEFT JOIN DIRECTORS director ON fd.director_id = director.id " +
                "JOIN LIKES l1 ON f.id = l1.film_id AND l1.user_id = ? " +
                "JOIN LIKES l2 ON f.id = l2.film_id AND l2.user_id = ?";

        return jdbc.query(query, filmResultExtractor, userId, friendId);
    }

    @Override
    public List<Film> search(String searchQuery, String params) {
        String[] searchParams = params.split(",");
        String resp = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DESCRIPTION, FILMS.RELEASE_DATE, \n" +
                "FILMS.DURATION, MPA.ID AS MPA_ID, MPA.NAME AS MPA_RATE, GENRE.ID AS GENRE_ID, \n" +
                "GENRE.NAME AS GENRE, \n" +
                "DIRECTORS.ID AS DIRECTOR_ID, DIRECTORS.NAME AS DIRECTOR_NAME FROM FILMS\n" +
                "LEFT JOIN MPA ON FILMS.MPA_RATE = MPA.ID\n" +
                "LEFT JOIN FILMS_GENRE ON FILMS.ID = FILMS_GENRE.FILM_ID\n" +
                "LEFT JOIN GENRE ON GENRE.ID = FILMS_GENRE.GENRE_ID\n" +
                "LEFT JOIN FILMS_DIRECTORS ON FILMS_DIRECTORS.FILM_ID = FILMS.ID\n" +
                "LEFT JOIN DIRECTORS ON DIRECTORS.ID = FILMS_DIRECTORS.DIRECTOR_ID\n" +
                "LEFT JOIN (SELECT LIKES.FILM_ID AS id_film, COUNT(LIKES.USER_ID) AS like_count FROM LIKES\n" +
                "GROUP BY FILM_ID) AS like_count ON like_count.id_film = FILMS.ID\n";
        List<Film> result = new ArrayList<>();

        if (searchParams.length == 1) {
            if (searchParams[0].equals("director")) {
                result = searchByDirector(resp, searchQuery);
            } else if (searchParams[0].equals("title")) {
                result = searchByTitle(resp, searchQuery);
            }
        } else if (searchParams.length == 2) {
            result = searchByBothParams(resp, searchQuery);
        }

        return result;
    }

    /**
     * Search films by title
     *
     * @param sqlQuery    SQL query string
     * @param searchQuery search string
     * @return List of films
     */
    private List<Film> searchByTitle(String sqlQuery, String searchQuery) {
        String query = sqlQuery + "WHERE FILMS.TITLE LIKE '%" + searchQuery + "%'\n" +
                "ORDER BY like_count.like_count DESC";
        return jdbc.query(query, filmResultExtractor);
    }

    /**
     * Search by director
     *
     * @param sqlQuery    SQL query string
     * @param searchQuery search string
     * @return List of films
     */
    private List<Film> searchByDirector(String sqlQuery, String searchQuery) {
        String query = sqlQuery + "WHERE DIRECTORS.NAME LIKE '%" + searchQuery + "%'\n" +
                "ORDER BY like_count.like_count DESC";
        return jdbc.query(query, filmResultExtractor);
    }

    /**
     * Search by both parameters (title, director)
     *
     * @param sqlQuery    SQL query string
     * @param searchQuery search string
     * @return List of films
     */
    private List<Film> searchByBothParams(String sqlQuery, String searchQuery) {
        String query = sqlQuery + "WHERE (FILMS.TITLE LIKE '%" + searchQuery + "%' OR DIRECTORS.NAME LIKE '%" +
                searchQuery + "%')\n" +
                "ORDER BY like_count.like_count DESC";
        return jdbc.query(query, filmResultExtractor);
    }

    @Override
    public List<Film> getRecommendations(Long id) {
        String querry = "SELECT FILM_ID ID FROM LIKES l \n" +
                "WHERE USER_ID = (SELECT l2.USER_ID FROM LIKES l, LIKES l2 \n" +
                "WHERE l.FILM_ID = L2.FILM_ID \n" +
                "AND l.USER_ID = ?\n" +
                "AND l.USER_ID != L2.USER_ID \n" +
                "GROUP BY l.FILM_ID, L2.USER_ID \n" +
                "ORDER BY COUNT(*) LIMIT 1)\n" +
                "AND FILM_ID NOT IN (SELECT FILM_ID FROM LIKES l2 WHERE USER_ID = ?)";

        return jdbc.query(querry, (rs, rowNum) -> getFilm(rs.getLong("ID")), id, id);
    }

    /**
     * Remove duplicate film genres
     *
     * @param film
     * @return Film
     * @throws BadRequest
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


}
