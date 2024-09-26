package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final DirectorStorage directorStorage;

    @Test
    public void addFilm() throws BadRequest {

        Genre genre = Genre.builder()
                .id(1)
                .name("Action")
                .build();
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genre);
        Film film = Film.builder()
                .name("Some film")
                .description("Descriptiom")
                .duration(120L)
                .releaseDate(LocalDate.of(2000, 01, 01))
                .mpa(MpaRating.builder()
                        .id(1)
                        .name("G")
                        .build())
                .genres(genreList)
                .build();

        Film filmFromDb = filmStorage.addNew(film);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(filmFromDb.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(assertFilm ->
                        assertThat(assertFilm).hasFieldOrPropertyWithValue("id", filmFromDb.getId())
                                .hasFieldOrPropertyWithValue("name", filmFromDb.getName())
                                .hasFieldOrPropertyWithValue("description", filmFromDb.getDescription())
                                .hasFieldOrPropertyWithValue("duration", filmFromDb.getDuration())
                                .hasFieldOrPropertyWithValue("releaseDate", filmFromDb.getReleaseDate())
                                .hasFieldOrPropertyWithValue("mpa", filmFromDb.getMpa())
                                .hasFieldOrPropertyWithValue("genres", filmFromDb.getGenres())
                );

    }

    @Test
    public void updateFilm() throws BadRequest, NotFoundException {

        Genre genre = Genre.builder()
                .id(1)
                .name("Action")
                .build();
        Director director = Director.builder()
                .id(1)
                .name("New director")
                .build();
        List<Genre> genreList = new ArrayList<>();
        List<Director> directorList = new ArrayList<>();
        genreList.add(genre);
        directorList.add(director);
        Film film = Film.builder()
                .name("Some film")
                .description("Descriptiom")
                .duration(120L)
                .releaseDate(LocalDate.of(2000, 01, 01))
                .mpa(MpaRating.builder()
                        .id(1)
                        .name("G")
                        .build())
                .genres(genreList)
                .build();

        directorStorage.add(director);
        Film filmFromDb = filmStorage.addNew(film);
        filmFromDb.setDescription("Changed description");
        filmFromDb.setDirectors(directorList);


        Film assertFilm = filmStorage.update(filmFromDb);
        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilm(assertFilm.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(filmDb ->
                        assertThat(filmDb).hasFieldOrPropertyWithValue("description",
                                "Changed description")
                );
    }

    @Test
    public void likeFilm() throws BadRequest {

        Genre genre = Genre.builder()
                .id(1)
                .name("Action")
                .build();
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genre);
        Film film = Film.builder()
                .name("Some film")
                .description("Descriptiom")
                .duration(120L)
                .releaseDate(LocalDate.of(2000, 01, 01))
                .mpa(MpaRating.builder()
                        .id(1)
                        .name("G")
                        .build())
                .genres(genreList)
                .build();

        Film filmFromDb = filmStorage.addNew(film);

        User user = User.builder()
                .name("Some User")
                .login("userLogin")
                .email("aaa@gmail.com")
                .birthday(LocalDate.of(1992, 01, 01))
                .build();

        User userFromDb = userStorage.addNew(user);

        filmStorage.addLike(filmFromDb.getId(), userFromDb.getId());
        List<Film> ratedFilms = filmStorage.getMostRated(1);
        assertThat(ratedFilms.getFirst()).isEqualTo(filmFromDb);

    }

}