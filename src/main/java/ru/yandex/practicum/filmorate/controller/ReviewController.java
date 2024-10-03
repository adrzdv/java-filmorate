package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.BadRequest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review addNew(@Valid @RequestBody Review newReview) throws BadRequest, NotFoundException {
        return reviewService.addNew(newReview);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) throws NotFoundException, BadRequest {
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReviewById(@PathVariable @Valid int id) throws EmptyResultDataAccessException {
        reviewService.deleteReviewById(id);
    }

    @GetMapping("/{id}")
    public Review getOne(@PathVariable @Valid int id) throws NotFoundException {
        return reviewService.getOne(id);
    }

    @GetMapping
    public List<Review> getReviews(
            @RequestParam(value = "filmId",required = false) @Valid Integer filmId,
            @RequestParam(value = "count", required = false) @Valid Integer count) {
        return reviewService.getReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeReview(@PathVariable @Valid int id,
                           @PathVariable @Valid int userId) throws NotFoundException {
        reviewService.likeReview(id,userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable @Valid int id,
                              @PathVariable @Valid int userId) throws NotFoundException {
        reviewService.dislikeReview(id,userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeReview(@PathVariable @Valid int id,
                                 @PathVariable @Valid int userId) throws NotFoundException {
        reviewService.deleteLikeReview(id,userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeReview(@PathVariable @Valid int id,
                                    @PathVariable @Valid int userId) throws NotFoundException {
        reviewService.deleteDislikeReview(id,userId);
    }
}
