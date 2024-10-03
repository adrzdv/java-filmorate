package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmResultExtractor implements ResultSetExtractor<List<Film>> {
    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> filmList = new LinkedHashMap<>();
        while (rs.next()) {
            if (filmList.containsKey(rs.getLong("id"))) {
                Genre genre = Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre"))
                        .build();
                if (genre.getId() != 0) {
                    if (!filmList.get(rs.getLong("id")).getGenres().contains(genre)) {
                        filmList.get(rs.getLong("id")).getGenres().add(genre);
                    }
                }

                Director director = Director.builder()
                        .id(rs.getInt("director_id"))
                        .name(rs.getString("director_name"))
                        .build();
                if (director.getId() != 0) {
                    if (!filmList.get(rs.getLong("id")).getDirectors().contains(director)) {
                        filmList.get(rs.getLong("id")).getDirectors().add(director);
                    }
                }
            } else {
                Genre genre = Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre"))
                        .build();
                Director director = Director.builder()
                        .id(rs.getInt("director_id"))
                        .name(rs.getString("director_name"))
                        .build();
                Film film = Film.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("title"))
                        .description(rs.getString("description"))
                        .duration(rs.getLong("duration"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .mpa(MpaRating.builder()
                                .id(rs.getInt("mpa_id"))
                                .name(rs.getString("mpa_rate"))
                                .build())
                        .genres(new ArrayList<>())
                        .directors(new ArrayList<>())
                        .build();
                filmList.put(film.getId(), film);
                if (director.getId() != 0) {
                    filmList.get(film.getId()).getDirectors().add(director);
                }
                if (genre.getId() != 0) {
                    filmList.get(film.getId()).getGenres().add(genre);
                }
            }
        }

        return new ArrayList<>(filmList.values());
    }
}