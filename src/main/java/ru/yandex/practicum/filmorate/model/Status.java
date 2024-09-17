package ru.yandex.practicum.filmorate.model;

/**
 * ENUM of friendship status.
 * Use to determine the status of a friendship status.
 * <p>
 * REQUESTED - User1 requested to add User2 to a friend list
 * APPROVED - User2 approved User1 request
 * DECLINED - User2 declined User1 request
 */
public enum Status {
    REQUESTED,
    APPROVED,
    DECLINED
}
