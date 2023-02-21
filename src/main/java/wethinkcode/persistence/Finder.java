package wethinkcode.persistence;

import wethinkcode.rdbms.GenresTableValidator;

import javax.naming.InsufficientResourcesException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 3.3
 */
public class Finder {

    private final Connection connection;

    /**
     * Create an instance of the Finder object using the provided database connection
     *
     * @param connection The JDBC connection to use
     */
    public Finder(Connection connection) {
        this.connection = connection;
    }

    /**
     * 3.3 (part 1) Complete this method
     * <p>
     * Finds all genres in the database
     *
     * @return a list of `Genre` objects
     * @throws SQLException the query failed
     */
    public List<Genre> findAllGenres() throws SQLException {
        List<Genre> genres = new ArrayList<>();
        try( final Statement statement = connection.createStatement()) {
            statement.execute("SELECT * FROM Genres");

            try (ResultSet results = statement.getResultSet()) {

                while (results.next()) {
                    final String code = results.getString("code");
                    final String description = results.getString("description");
                    genres.add(new Genre(code, description));
                }
            }
        }
        return genres;
    }

    /**
     * 3.3 (part 2) Complete this method
     * <p>
     * Finds all genres in the database that have specific substring in the description
     *
     * @param pattern The pattern to match
     * @return a list of `Genre` objects
     * @throws SQLException the query failed
     */
    public List<Genre> findGenresLike(String pattern) throws SQLException {

        List<Genre> genres = new ArrayList<>();
        try( final Statement statement = connection.createStatement()) {
            boolean gotResult = statement.execute("SELECT * FROM Genres WHERE Genres.description LIKE '%"+ pattern + "%';");

            if (!gotResult){

            }else{
                try (ResultSet results = statement.getResultSet()) {
                    while (results.next()) {
                        final String code = results.getString("code");
                        final String description = results.getString("description");
                        genres.add(new Genre(code, description));
                    }
                }
            }
        }

        return genres;
    }

    /**
     * 3.3 (part 3) Complete this method
     * <p>
     * Finds all books with their genres
     *
     * @return a list of `BookGenreView` objects
     * @throws SQLException the query failed
     */
    public List<BookGenreView> findBooksAndGenres() throws SQLException {
        List<BookGenreView> titleGenre = new ArrayList<>();

        try (final Statement statement = connection.createStatement()) {
            boolean gotResult = statement.execute("SELECT Books.title, Genres.description\n" +
                    "FROM Books\n" +
                    "INNER JOIN Genres ON Books.genre_code=Genres.code;");

            if (!gotResult) {

            } else {
                try (ResultSet results = statement.getResultSet()) {
                    while (results.next()) {
                        titleGenre.add(new BookGenreView(results.getString("title"), results.getString("description")));
                    }
                }
            }

            return titleGenre;
        }
    }

    /**
     * 3.3 (part 4) Complete this method
     * <p>
     * Finds the number of books in a genre
     *
     * @return the number of books in the genre
     * @throws SQLException the query failed
     */
    public int findNumberOfBooksInGenre(String genreCode) throws SQLException {
        List<Genre> genreList = new ArrayList<>();

        try( final PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Books, Genres WHERE Books.genre_code = Genres.code AND Books.genre_code = ?"))
        {
            statement.setString(1, genreCode);
            boolean gotResults = statement.execute();

            if (!gotResults){
                throw new RuntimeException("Nothing found");
            }else{
                try (ResultSet results = statement.getResultSet()) {
                    while (results.next()) {
                        genreList.add(new Genre(results.getString("code"), results.getString("description")));
                    }
                }
            }
            return genreList.size();
        }
    }
}