package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ComponentScan("ru.yandex.practicum.filmorate")
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void addUser() {

        User user = User.builder()
                .name("Some User")
                .login("userLogin")
                .email("aaa@gmail.com")
                .birthday(LocalDate.of(1992, 01, 01))
                .build();

        User assertUser = userStorage.addNew(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(assertUser.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(userFromDb ->
                        assertThat(userFromDb).hasFieldOrPropertyWithValue("id", 1L)
                );

    }

    @Test
    public void updateUser() throws NotFoundException {

        User user = User.builder()
                .name("Some User")
                .login("userLogin")
                .email("aaa@gmail.com")
                .birthday(LocalDate.of(1992, 01, 01))
                .build();

        User userForUpdate = userStorage.getUser(userStorage.addNew(user).getId());
        userForUpdate.setLogin("anotherLogin");

        User assertUser = userStorage.update(userForUpdate);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(assertUser.getId()));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(userFromDb ->
                        assertThat(userFromDb).hasFieldOrPropertyWithValue("login", "anotherLogin")
                );
    }

    @Test
    public void addAndRemoveFriend() throws NotFoundException {

        User user1 = User.builder()
                .name("Some User")
                .login("userLogin")
                .email("aaa@gmail.com")
                .birthday(LocalDate.of(1992, 01, 01))
                .build();

        User user2 = User.builder()
                .name("Another User")
                .login("someLogin")
                .email("bbbbb@gmail.com")
                .birthday(LocalDate.of(1992, 01, 01))
                .build();

        User userFromDb1 = userStorage.addNew(user1);
        User userFromDb2 = userStorage.addNew(user2);

        userStorage.addFriend(userFromDb1.getId(), userFromDb2.getId());

        List<User> friendList = userStorage.getFriends(userFromDb1.getId());
        assertThat(friendList.getFirst()).hasFieldOrPropertyWithValue("id", userFromDb2.getId());

        userStorage.deleteFriend(userFromDb1.getId(), userFromDb2.getId());
        friendList = userStorage.getFriends(userFromDb1.getId());
        assertThat(friendList.size()).isEqualTo(0);

    }

}