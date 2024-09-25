package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> getAll() {

        return directorStorage.getAll();
    }

    public Director getOne(int id) throws NotFoundException {

        return directorStorage.getOne(id);
    }

    public Director add(Director director) throws NotFoundException {

        return directorStorage.add(director);
    }

    public Director update(Director director) throws NotFoundException {

        return directorStorage.update(director);
    }

    public void delete(int id) throws NotFoundException {

        directorStorage.delete(id);
    }
}
