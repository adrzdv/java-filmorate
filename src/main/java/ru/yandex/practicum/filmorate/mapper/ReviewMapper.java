package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReviewMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        String reviewType = rs.getString("review_type");
        boolean isPositive = reviewType.equalsIgnoreCase("positive");

        return Review.builder()
                .id(rs.getInt("id"))
                .isPositive(isPositive)
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .content(rs.getString("review_content"))
                .usefulRate(rs.getInt("useful_rate"))
                .build();
    }
}
