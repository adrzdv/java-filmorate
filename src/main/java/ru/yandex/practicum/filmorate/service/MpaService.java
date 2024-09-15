package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<MpaRating> getAll() {

        return mpaStorage.getAll();
    }

    public MpaRating getMpa(int id) throws NotFoundException {

        return mpaStorage.getOne(id);
    }
}
