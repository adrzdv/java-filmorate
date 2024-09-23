package ru.yandex.practicum.filmorate.model;

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
    private int id;
    private String content;
    private int usefulRate;
    private int userId;
    private int filmId;
}
