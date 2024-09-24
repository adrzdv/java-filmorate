package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        List<Genre> genreList = new ArrayList<>();
        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("title"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getLong("duration"))
                .mpa(MpaRating.builder()
                        .id(rs.getInt("mpa_id")) // Убедитесь, что столбец mpa_id существует в запросе
                        .name(rs.getString("mpa_rate")) // Убедитесь, что столбец mpa_rate существует в запросе
                        .build())
                .build();

        // Теперь добавляем жанры в список, если они есть в результате
        do {
            if (rs.getInt("genre_id") > 0) { // Проверяем, что genre_id больше 0
                Genre genre = Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(rs.getString("genre"))
                        .build();
                genreList.add(genre);
            }
        } while (rs.next());

        film.setGenres(genreList); // Устанавливаем жанры в фильм
        return film;
    }
}
