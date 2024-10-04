package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    public List<Review> getAll() {
        return reviewStorage.getAll();
    }

    public Review getOne(int id) throws NotFoundException {
        return reviewStorage.getOne(id);
    }

    public Review addNew(Review newReview) throws BadRequest, NotFoundException {

        eventStorage.createEvent(newReview.getUserId(), EventType.REVIEW, Operations.ADD, newReview.getReviewId());
        return reviewStorage.addNew(newReview);
    }

    public Review update(Review review) throws NotFoundException, BadRequest {
        eventStorage.createEvent(review.getUserId(), EventType.REVIEW, Operations.UPDATE, review.getReviewId());
        return reviewStorage.update(review);
    }

    public void deleteReviewById(int id) throws EmptyResultDataAccessException, NotFoundException {
        reviewStorage.deleteReviewById(id);
        eventStorage.createEvent(reviewStorage.getOne(id).getUserId(), EventType.REVIEW, Operations.REMOVE,reviewStorage.getOne(id).getReviewId());
    }

    public List<Review> getReviews(Integer filmId, Integer count) {
        return reviewStorage.getReviews(filmId,count);
    }

    public void likeReview(int reviewId, int userId) throws NotFoundException {
        reviewStorage.likeReview(reviewId,userId);
    }

    public void dislikeReview(int reviewId, int userId) throws NotFoundException {
        reviewStorage.dislikeReview(reviewId,userId);
    }

    public void deleteLikeReview(int reviewId, int userId) throws NotFoundException {
        reviewStorage.deleteLikeReview(reviewId,userId);
    }

    public void deleteDislikeReview(int reviewId, int userId) throws NotFoundException {
        reviewStorage.deleteDislikeReview(reviewId,userId);
    }
}
