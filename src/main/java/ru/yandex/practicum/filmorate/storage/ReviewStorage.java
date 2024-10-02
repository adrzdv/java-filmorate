package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Review;


import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ReviewStorage implements BaseStorage {
    private final JdbcTemplate jdbc;
    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;
    private final FilmMapper filmMapper;

    @Override
    public List<Review> getAll() {
        String query = "SELECT * FROM REVIEWS";
        return jdbc.query(query,reviewMapper);
    }

    @Override
    public Review getOne(int id) throws NotFoundException {
        String query = "SELECT * FROM REVIEWS WHERE ID = ?";
        try {
            Optional<Review> reviewOptional = Optional.ofNullable(jdbc.queryForObject(query,reviewMapper,id));
            if (reviewOptional.isEmpty()) {
                throw new NotFoundException("Review not found");
            }
            return reviewOptional.get();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Review not found");
        }
    }

    public Review addNew(Review newReview) throws BadRequest, NotFoundException {

        String queryUser = "SELECT COUNT(*) FROM USERS WHERE ID = ?";
        Integer userCount = jdbc.queryForObject(queryUser, Integer.class, newReview.getUserId());

        if (userCount == null || userCount == 0) {
            throw new BadRequest("User with ID " + newReview.getUserId() + " does not exist.");
        }

        String queryFilm = "SELECT COUNT(*) FROM FILMS WHERE ID = ?";
        Integer filmCount = jdbc.queryForObject(queryFilm, Integer.class, newReview.getFilmId());

        if (filmCount == null || filmCount == 0) {
            throw new BadRequest("Film with ID " + newReview.getFilmId() + " does not exist.");
        }

        String query = "INSERT INTO REVIEWS (REVIEW_CONTENT, USER_ID, FILM_ID, REVIEW_TYPE, USEFUL_RATE)" +
                " VALUES (?, ?, ?, ?, 10)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1, newReview.getContent());
            ps.setInt(2, newReview.getUserId());
            ps.setInt(3, newReview.getFilmId());
            ps.setString(4, newReview.isPositive() ? "positive" : "negative");
            return ps;
        }, keyHolder);

        int key = keyHolder.getKey().intValue();

        return getOne(key);
    }


    public Review update(Review review) throws NotFoundException {

        String selectReviewQuery = "SELECT COUNT(*) FROM REVIEWS WHERE ID = ?";
        Integer reviewCount = jdbc.queryForObject(selectReviewQuery, Integer.class, review.getId());

        if (reviewCount == null || reviewCount == 0) {
            throw new NotFoundException("Review not found with id: " + review.getId());
        }

        String selectUserQuery = "SELECT COUNT(*) FROM USERS WHERE ID = ?";
        Integer userCount = jdbc.queryForObject(selectUserQuery, Integer.class, review.getUserId());

        if (userCount == null || userCount == 0) {
            throw new NotFoundException("User not found with id: " + review.getUserId());
        }

        String selectFilmQuery = "SELECT COUNT(*) FROM FILMS WHERE ID = ?";
        Integer filmCount = jdbc.queryForObject(selectFilmQuery, Integer.class, review.getFilmId());

        if (filmCount == null || filmCount == 0) {
            throw new NotFoundException("Film not found with id: " + review.getFilmId());
        }

        String query = "UPDATE REVIEWS SET REVIEW_CONTENT = ?, REVIEW_TYPE = ?, USEFUL_RATE = ? WHERE ID = ?";
        int res = jdbc.update(query, review.getContent(), review.isPositive() ? "positive" : "negative", review.getUsefulRate(), review.getId());

        if (res != 1) {
            throw new NotFoundException("Failed to update review with id: " + review.getId());
        }

        return getOne(review.getId());
    }

    public void deleteReviewById(int id) throws EmptyResultDataAccessException {

        String query = "DELETE FROM REVIEWS WHERE ID = ?";
        jdbc.update(query,id);
    }

    public List<Review> getReviews(Integer filmId, Integer count) {
        if (count == null || count <= 0) {
            count = 10;
        }

        String query;
        Object[] params;

        if (filmId != null) {
            query = "SELECT * FROM REVIEWS WHERE FILM_ID = ? ORDER BY USEFUL_RATE DESC LIMIT ?";
            params = new Object[]{filmId, count};
        } else {
            query = "SELECT * FROM REVIEWS ORDER BY USEFUL_RATE DESC LIMIT ?";
            params = new Object[]{count};
        }

        return jdbc.query(query, reviewMapper, params);
    }

    public boolean userExists(int userId) {
        String query = "SELECT COUNT(*) FROM USERS WHERE ID = ?";

        Integer count = jdbc.queryForObject(query, Integer.class, userId);

        return count != null && count > 0;
    }

    public boolean reviewExists(int reviewId) {
        String query = "SELECT COUNT(*) FROM REVIEWS WHERE ID = ?";

        Integer count = jdbc.queryForObject(query, Integer.class, reviewId);

        return count != null && count > 0;
    }

    public void likeReview(int reviewId, int userId) throws NotFoundException {
        if (!userExists(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!reviewExists(reviewId)) {
            throw new NotFoundException("Review not found");
        }

        String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE + 1 WHERE ID = ?";

        int res = jdbc.update(query, reviewId);

        if (res!=1) {
            throw new NotFoundException("Review not found ");
        }
    }

    public void dislikeReview(int reviewId, int userId) throws NotFoundException {
        if (!userExists(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!reviewExists(reviewId)) {
            throw new NotFoundException("Review not found");
        }

        String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE - 1 WHERE ID = ?";

        int res = jdbc.update(query, reviewId);

        if (res!=1) {
            throw new NotFoundException("Review not found ");
        }
    }

    public void deleteDislikeReview(int reviewId, int userId) throws NotFoundException {
        if (!userExists(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!reviewExists(reviewId)) {
            throw new NotFoundException("Review not found");
        }

        String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE + 1 WHERE ID = ?";

        int res = jdbc.update(query, reviewId);

        if (res!=1) {
            throw new NotFoundException("Review not found ");
        }
    }

    public void deleteLikeReview(int reviewId, int userId) throws NotFoundException {
        if (!userExists(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!reviewExists(reviewId)) {
            throw new NotFoundException("Review not found");
        }

        String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE - 1 WHERE ID = ?";

        int res = jdbc.update(query, reviewId);

        if (res!=1) {
            throw new NotFoundException("Review not found ");
        }
    }
}
