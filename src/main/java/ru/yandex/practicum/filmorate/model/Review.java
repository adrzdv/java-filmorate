package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Review object class
 * <p>
 * id - identification number of review
 * content - text of review
 * usefulRate - current review rating
 * userId - id of a review's author
 * filmId - reviewed film's id
 */

@Data
@AllArgsConstructor
@Builder
public class Review {
    @NotNull(message = "Review ID cannot be null")
    private int reviewId;
    @NotBlank(message = "Content cannot be blank")
    private String content;
    private int useful;
    @NotNull(message = "User ID cannot be null")
    private int userId;
    @NotNull(message = "Film ID cannot be null")
    private int filmId;
    @JsonProperty("isPositive")
    private Boolean isPositive;
}


