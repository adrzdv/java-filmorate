# java-filmorate
Template repository for Filmorate project.

**Database schema**

Database schema link: https://dbdiagram.io/d/filmorate-66c0c3a38b4bb5230e5e743f

![](https://github.com/adrzdv/java-filmorate/blob/main/filmorate.png)

**Get all films liked by users**
```
SELECT users.name, films.title
FROM users
INNER JOIN likes ON likes.user_id=users.id
LEFT JOIN films ON films.id=likes.film_id
ORDER BY users.name;
```

**Get a user's friends count**
```
SELECT users.login AS user, COUNT(friends.friend_id)
FROM users
LEFT JOIN friends ON friends.user_id = users.user_id
GROUP BY user;
```

**Get a genre's film count**
```
SELECT genre.name, COUNT(films_genre.film_id) AS count
FROM genre
LEFT JOIN films_genre ON films_genre.genre_id=genre.id
GROUP BY genre.name;
```

***Get a film rate by users***
```
SELECT films.title, COUNT (likes.user_id) AS rate
FROM films
INNER JOIN likes ON likes.film_id=films.id
GROUP BY films.title;
```


