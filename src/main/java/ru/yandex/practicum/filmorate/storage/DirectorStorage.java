package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DirectorStorage implements BaseStorage {

    private final JdbcTemplate jdbc;
    private final DirectorMapper mapper;


    @Override
    public List<Director> getAll() {
        String query = "SELECT * FROM DIRECTORS ORDER BY ID";
        return jdbc.query(query, mapper);
    }

    @Override
    public Director getOne(int id) throws NotFoundException {
        String query = "SELECT * FROM DIRECTORS WHERE ID = ?";
        try {
            Optional<Director> directorOptional = Optional.ofNullable(jdbc.queryForObject(query, mapper, id));
            if (directorOptional.isEmpty()) {
                throw new NotFoundException("Director not found");
            }
            return directorOptional.get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Director not found");
        }
    }

    /**
     * Add a new director
     *
     * @param director new director object
     */

    public Director add(Director director) throws NotFoundException {
        String query = "INSERT INTO DIRECTORS (NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String finalQuery = query;

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(finalQuery, new String[]{"id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);

        int key = keyHolder.getKey().intValue();

        return getOne(key);
    }

    /**
     * Update an existing director
     *
     * @param director an updated director object
     */
    public Director update(Director director) throws NotFoundException {

        String query = "UPDATE DIRECTORS SET NAME = ? WHERE ID = ?";
        int res = jdbc.update(query, director.getName(), director.getId());
        if (res != 1) {
            throw new NotFoundException("Director not found");
        }
        return getOne(director.getId());
    }

    /**
     * Delete an existing director
     *
     * @param id identification number of director to delete
     */
    public void delete(int id) throws NotFoundException {

        String query = "DELETE FROM DIRECTORS WHERE ID = ?";
        jdbc.update(query, id);

    }
}
