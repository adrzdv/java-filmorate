package ru.yandex.practicum.filmorate.mapper;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FilmRatedMapper implements RowMapper<Film> {
    private JdbcTemplate jdbcTemplate;
    private GenreMapper genreMapper;
    private DirectorMapper directorMapper;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        long filmId = rs.getLong("id");
        List<Genre> genres = jdbcTemplate.query("SELECT g.id, g.name FROM GENRE g " +
                        "JOIN FILMS_GENRE fg ON g.id = fg.genre_id WHERE fg.film_id = ?",
                genreMapper, filmId);

        List<Director> directors = jdbcTemplate.query("SELECT DIRECTORS.ID, DIRECTORS.NAME FROM DIRECTORS " +
                "LEFT JOIN FILMS_DIRECTORS ON FILMS_DIRECTORS.DIRECTOR_ID = DIRECTORS.ID " +
                "WHERE FILM_ID = ?", directorMapper, filmId);

        return Film.builder()
                .id(filmId)
                .name(rs.getString("title"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .mpa(MpaRating.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_rate"))
                        .build())
                .genres(genres)
                .directors(directors)
                .build();

    }
}
