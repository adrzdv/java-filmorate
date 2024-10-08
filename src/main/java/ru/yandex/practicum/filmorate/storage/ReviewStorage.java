package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;


import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ReviewStorage implements BaseStorage {
    private final JdbcTemplate jdbc;
    private final ReviewMapper reviewMapper;

    /**
     * Method for getting all reviews
     *
     * @return list of reviews
     */
    @Override
    public List<Review> getAll() {
        String query = "SELECT * FROM REVIEWS";
        return jdbc.query(query, reviewMapper);
    }

    /**
     * Method for getting one review by id
     *
     * @param reviewId int review id
     * @return object of Review
     * @throws NotFoundException
     */
    @Override
    public Review getOne(int reviewId) throws NotFoundException {
        String query = "SELECT * FROM REVIEWS WHERE ID = ?";
        Optional<Review> review = Optional.ofNullable(jdbc.queryForObject(query, reviewMapper, reviewId));
        if (review.isEmpty()) {
            throw new NotFoundException("Review not found with id: " + reviewId);
        }

        try {
            return jdbc.queryForObject(query, reviewMapper, reviewId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Review not found with id: " + reviewId);
        }
    }

    /**
     * Method for adding new review
     *
     * @param newReview object of review
     * @return added review
     * @throws BadRequest
     * @throws NotFoundException
     */
    public Review addNew(Review newReview) throws BadRequest, NotFoundException {
        if (newReview.getUserId() < 0) {
            throw new NotFoundException("Invalid user ID: " + newReview.getUserId());
        }

        if (newReview.getFilmId() < 0) {
            throw new NotFoundException("Invalid film ID: " + newReview.getFilmId());
        }

        if (newReview.getIsPositive() == null) {
            throw new BadRequest("Invalid review_type ID: " + newReview.getReviewId());
        }

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
                " VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1, newReview.getContent());
            ps.setInt(2, newReview.getUserId());
            ps.setInt(3, newReview.getFilmId());
            ps.setString(4, newReview.getIsPositive() ? "positive" : "negative");
            ps.setInt(5, 0);
            return ps;
        }, keyHolder);

        int key = keyHolder.getKey().intValue();

        return getOne(key);
    }

    /**
     * Method for updating existing review
     *
     * @param review object of review
     * @return updated review
     * @throws NotFoundException
     * @throws BadRequest
     */

    public Review update(Review review) throws NotFoundException, BadRequest {

        if (review.getUserId() <= 0) {
            throw new BadRequest("Invalid user ID: " + review.getUserId());
        }

        if (review.getFilmId() <= 0) {
            throw new BadRequest("Invalid film ID: " + review.getFilmId());
        }

        String selectReviewQuery = "SELECT COUNT(*) FROM REVIEWS WHERE ID = ?";
        Integer reviewCount = jdbc.queryForObject(selectReviewQuery, Integer.class, review.getReviewId());

        if (reviewCount == null || reviewCount == 0) {
            throw new NotFoundException("Review not found with id: " + review.getReviewId());
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

        String query = "UPDATE REVIEWS SET REVIEW_CONTENT = ?, REVIEW_TYPE = ? WHERE ID = ?";
        int res = jdbc.update(query, review.getContent(), review.getIsPositive() ? "positive" : "negative",
                review.getReviewId());

        if (res != 1) {
            throw new NotFoundException("Failed to update review with id: " + review.getReviewId());
        }


        return getOne(review.getReviewId());
    }

    /**
     * Method for removing review
     *
     * @param id id of review
     * @throws EmptyResultDataAccessException
     */
    public void deleteReviewById(int id) throws EmptyResultDataAccessException {

        String query = "DELETE FROM REVIEWS WHERE ID = ?";
        jdbc.update(query, id);
    }

    /**
     * Method to get all reviews
     *
     * @param filmId id of film
     * @param count  count of films that need to be taken out
     * @return list of reviews
     */
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

    /**
     * Method to like review
     *
     * @param reviewId id of review
     * @param userId   id of user
     * @throws NotFoundException
     */
    public void likeReview(int reviewId, int userId) throws NotFoundException {
        if (!userExists(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!reviewExists(reviewId)) {
            throw new NotFoundException("Review not found");
        }

        String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE + 1 WHERE ID = ?";

        int res = jdbc.update(query, reviewId);

        if (res != 1) {
            throw new NotFoundException("Review not found ");
        }

    }

    /**
     * Method fot dislike review
     *
     * @param reviewId id of review
     * @param userId   id of user
     * @throws NotFoundException
     */
    public void dislikeReview(int reviewId, int userId) throws NotFoundException {
        if (!userExists(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!reviewExists(reviewId)) {
            throw new NotFoundException("Review not found");
        }

        String usefulRateQuery = "SELECT USEFUL_RATE FROM REVIEWS WHERE ID = ?";

        Integer usefulRate = jdbc.queryForObject(usefulRateQuery, Integer.class, reviewId);

        if (usefulRate != 0) {
            String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE - 2 WHERE ID = ?";
            int res = jdbc.update(query, reviewId);
        } else {
            String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE - 1 WHERE ID = ?";

            int res = jdbc.update(query, reviewId);

            if (res != 1) {
                throw new NotFoundException("Review not found ");
            }
        }
    }

    /**
     * Method to delete dislike
     *
     * @param reviewId id of review
     * @param userId   id of user
     * @throws NotFoundException
     */
    public void deleteDislikeReview(int reviewId, int userId) throws NotFoundException {
        if (!userExists(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!reviewExists(reviewId)) {
            throw new NotFoundException("Review not found");
        }

        String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE + 1 WHERE ID = ?";

        int res = jdbc.update(query, reviewId);

        if (res != 1) {
            throw new NotFoundException("Review not found ");
        }
    }

    /**
     * Method to delete like
     *
     * @param reviewId id of review
     * @param userId   id of user
     * @throws NotFoundException
     */
    public void deleteLikeReview(int reviewId, int userId) throws NotFoundException {
        if (!userExists(userId)) {
            throw new NotFoundException("User not found");
        }
        if (!reviewExists(reviewId)) {
            throw new NotFoundException("Review not found");
        }

        String query = "UPDATE REVIEWS SET USEFUL_RATE = USEFUL_RATE - 1 WHERE ID = ?";

        int res = jdbc.update(query, reviewId);

        if (res != 1) {
            throw new NotFoundException("Review not found ");
        }
    }

    /**
     * Method to check if the user exists
     *
     * @param userId id of user
     * @return boolean
     */
    private boolean userExists(int userId) {
        String query = "SELECT COUNT(*) FROM USERS WHERE ID = ?";

        Integer count = jdbc.queryForObject(query, Integer.class, userId);

        return count != null && count > 0;
    }

    /**
     * Method to check if the review exists
     *
     * @param reviewId id of review
     * @return boolean
     */
    private boolean reviewExists(int reviewId) {
        String query = "SELECT COUNT(*) FROM REVIEWS WHERE ID = ?";

        Integer count = jdbc.queryForObject(query, Integer.class, reviewId);

        return count != null && count > 0;
    }
}
