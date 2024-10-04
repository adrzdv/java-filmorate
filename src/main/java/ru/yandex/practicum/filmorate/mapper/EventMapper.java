package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operations;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EventMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .entityId(rs.getInt("entity_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operations(Operations.valueOf(rs.getString("operation")))
                .timestamp(rs.getLong("timestamp"))
                .build();
    }
}
