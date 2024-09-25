package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
@AllArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    /**
     * Add a new director
     *
     * @param director director object
     * @return director object
     * @throws NotFoundException
     */
    @PostMapping
    public Director add(@RequestBody Director director) throws NotFoundException {

        return directorService.add(director);
    }

    /**
     * Update an existing director
     *
     * @param director director object for updaye
     * @return updated director object
     * @throws NotFoundException
     */
    @PutMapping
    public Director update(@RequestBody Director director) throws NotFoundException {

        return directorService.update(director);
    }

    /**
     * Get all existing directors
     *
     * @return List of director objects
     */
    @GetMapping
    public List<Director> getAll() {

        return directorService.getAll();
    }

    /**
     * Get a director by identification number
     *
     * @param id identification number of director
     * @return director object
     * @throws NotFoundException
     */
    @GetMapping("/{id}")
    @ResponseBody
    public Director getOne(@PathVariable int id) throws NotFoundException {

        return directorService.getOne(id);
    }

    /**
     * Delete existing director
     *
     * @param id identification number of director for delete
     * @throws NotFoundException
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id) throws NotFoundException {

        directorService.delete(id);
    }
}
