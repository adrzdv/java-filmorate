package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;

    public List<Review> getAll() {
        return reviewStorage.getAll();
    }

    public Review getOne(int id) throws NotFoundException {
        return reviewStorage.getOne(id);
    }

    public Review addNew(Review newReview) throws BadRequest, NotFoundException {
        return reviewStorage.addNew(newReview);
    }

    public Review update(Review review) throws NotFoundException, BadRequest {
        return reviewStorage.update(review);
    }

    public void deleteReviewById(int id) throws EmptyResultDataAccessException {
        reviewStorage.deleteReviewById(id);
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
