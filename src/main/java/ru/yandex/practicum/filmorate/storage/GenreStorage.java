package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class GenreStorage implements BaseStorage {
    private final JdbcTemplate jdbc;
    private final GenreMapper genreMapper;

    @Override
    public List<Genre> getAll() {
        String query = "SELECT * FROM GENRE ORDER BY ID";
        return jdbc.query(query, genreMapper);
    }

    @Override
    public Genre getOne(int id) throws NotFoundException {
        String query = "SELECT * FROM GENRE WHERE ID = ?";
        try {
            Optional<Genre> genreOptional = Optional.ofNullable(jdbc.queryForObject(query, genreMapper, id));
            if (genreOptional.isEmpty()) {
                throw new NotFoundException("Genre not found");
            }
            return genreOptional.get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Genre not found");
        }
    }
}
