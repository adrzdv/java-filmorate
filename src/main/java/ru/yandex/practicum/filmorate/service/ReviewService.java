package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final FeedStorage feedStorage;

    public List<Review> getAll() {

        return reviewStorage.getAll();
    }

    public Review getOne(int id) throws NotFoundException {

        return reviewStorage.getOne(id);
    }

    public Review addNew(Review newReview) throws BadRequest, NotFoundException {

        Review review = reviewStorage.addNew(newReview);
        feedStorage.createEvent(review.getUserId(), EventType.REVIEW, Operations.ADD, review.getReviewId());
        return review;
    }

    public Review update(Review review) throws NotFoundException, BadRequest {
        feedStorage.createEvent(review.getUserId(), EventType.REVIEW, Operations.UPDATE, review.getReviewId());
        return reviewStorage.update(review);
    }

    public void deleteReviewById(int id) throws EmptyResultDataAccessException, NotFoundException, BadRequest {
        feedStorage.createEvent(getOne(id).getUserId(), EventType.REVIEW, Operations.REMOVE, id);
        reviewStorage.deleteReviewById(id);
    }

    public List<Review> getReviews(Integer filmId, Integer count) {

        return reviewStorage.getReviews(filmId, count);
    }

    public void likeReview(int reviewId, int userId) throws NotFoundException, BadRequest {
        feedStorage.createEvent(userId, EventType.REVIEW, Operations.UPDATE, reviewId);
        reviewStorage.likeReview(reviewId, userId);
    }

    public void dislikeReview(int reviewId, int userId) throws NotFoundException, BadRequest {
        feedStorage.createEvent(userId, EventType.REVIEW, Operations.UPDATE, reviewId);
        reviewStorage.dislikeReview(reviewId, userId);
    }

    public void deleteLikeReview(int reviewId, int userId) throws NotFoundException, BadRequest {
        feedStorage.createEvent(userId, EventType.REVIEW, Operations.UPDATE, reviewId);
        reviewStorage.deleteLikeReview(reviewId, userId);
    }

    public void deleteDislikeReview(int reviewId, int userId) throws NotFoundException, BadRequest {
        feedStorage.createEvent(userId, EventType.REVIEW, Operations.UPDATE, reviewId);
        reviewStorage.deleteDislikeReview(reviewId, userId);
    }
}
