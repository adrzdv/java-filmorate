package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        List<Genre> genreList = new ArrayList<>();
        List<Director> directorList = new ArrayList<>();
        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("title"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .genres(genreList)
                .directors(directorList)
                .mpa(MpaRating.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_rate"))
                        .build())
                .build();

        do {

            Genre genre = Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("genre"))
                    .build();
            if (!genreList.contains(genre) && rs.getInt("genre_id") != 0) {
                genreList.add(genre);
            }

            Director director = Director.builder()
                    .id(rs.getInt("director_id"))
                    .name(rs.getString("director_name"))
                    .build();
            if (!directorList.contains(director) && rs.getInt("director_id") != 0) {
                directorList.add(director);
            }

        } while (rs.next());
        film.setGenres(genreList);
        film.setDirectors(directorList);
        return film;
    }
}