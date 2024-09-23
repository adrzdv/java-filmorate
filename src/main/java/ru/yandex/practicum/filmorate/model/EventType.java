package ru.yandex.practicum.filmorate.model;

/**
 * ENUM of event type
 * <p>
 * LIKE - user liked an existing film
 * REVIEW - user reviewed film
 * FRIEND - user added/removed friend or approved friendship
 */

public enum EventType {
    LIKE,
    REVIEW,
    FRIEND
}
