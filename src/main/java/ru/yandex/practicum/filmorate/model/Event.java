package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {

    private long eventId;
    private long userId;
    private long entityId;
    private EventType eventType;
    private Operations operations;
    private long timestamp;
}
