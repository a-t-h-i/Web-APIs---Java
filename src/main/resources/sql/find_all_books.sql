SELECT Books.title, Genres.description
FROM Books
INNER JOIN Genres ON Books.genre_code=Genres.code;