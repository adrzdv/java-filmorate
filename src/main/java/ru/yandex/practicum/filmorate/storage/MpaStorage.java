package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class MpaStorage implements BaseStorage {

    private final JdbcTemplate jdbc;
    private final MpaMapper mpaMapper;

    @Override
    public List<MpaRating> getAll() {
        String query = "SELECT * FROM MPA ORDER BY ID";
        return jdbc.query(query, mpaMapper);
    }

    @Override
    public MpaRating getOne(int id) throws NotFoundException {
        String query = "SELECT * FROM MPA WHERE ID = ?";
        try {
            Optional<MpaRating> mpaOptional =  Optional.ofNullable(jdbc.queryForObject(query, mpaMapper, id));
            if (mpaOptional.isEmpty()) {
                throw new NotFoundException("MPA not found");
            }
            return mpaOptional.get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("MPA not found");
        }
    }
}
