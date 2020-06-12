package bg.codeacademy.spring.digitallibrary.repository;

import bg.codeacademy.spring.digitallibrary.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LibraryRepository implements LibraryDao
{
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public LibraryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public void addBookToLibrary(Long userId, Long bookId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("user_id", userId);
    mapSqlParameterSource.addValue("book_id", bookId);

    namedParameterJdbcTemplate.update(
        "INSERT INTO TBL_LIBRARY (USER_ID, BOOK_ID) VALUES(:user_id, :book_id)",
        mapSqlParameterSource);
  }

  @Override
  public List<Book> findAllBooksInLibrary(Long userId, String orderCriteria)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("orderCriteria", orderCriteria)
        .addValue("user_id", userId);


    return namedParameterJdbcTemplate.query(
        "SELECT b.BOOK_ID, b.TITLE, b.GENRE, b.AUTHOR, b.ISENABLED " +
            "FROM TBL_BOOKS b " +
            "INNER JOIN TBL_LIBRARY l " +
            "ON b.BOOK_ID = l.BOOK_ID " +
            "WHERE USER_ID = :user_id " +
            "ORDER BY LOWER(:orderCriteria);",
        mapSqlParameterSource,
        (rs, rowNum) ->
            new Book()
                .setId(rs.getLong("book_id"))
                .setTitle(rs.getString("title"))
                .setGenre(rs.getString("genre"))
                .setAuthor(rs.getString("author"))
                .setEnabled(rs.getBoolean("isEnabled"))

    );
  }

  @Override
  public void deleteBookFromLibrary(Long userId, Long bookId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", userId)
        .addValue("book_id", bookId);

    try {
      namedParameterJdbcTemplate.update(
          "DELETE FROM  TBL_LIBRARY " +
              "WHERE USER_ID = :user_id " +
              "AND BOOK_ID = :book_id",
          mapSqlParameterSource);
    }
    catch (EmptyResultDataAccessException ex) {
    }
  }

  @Override
  public Boolean containsUser(Long userId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", userId);
    return namedParameterJdbcTemplate.queryForObject(
        "SELECT EXISTS(" +
            "SELECT USER_ID " +
            "FROM TBL_LIBRARY " +
            "WHERE USER_ID = :user_id" +
            "); ",
        mapSqlParameterSource, Boolean.class
    );
  }

  @Override
  public int deleteUser(Long userId)
  {

    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", userId);
    return namedParameterJdbcTemplate.update(
        "DELETE " +
            "FROM TBL_LIBRARY " +
            "WHERE USER_ID = :user_id; ",
        mapSqlParameterSource);
  }

  @Override
  public Boolean containsBook(Long bookId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("book_id", bookId);
    return namedParameterJdbcTemplate.queryForObject(
        "SELECT EXISTS(" +
            "SELECT BOOK_ID " +
            "FROM TBL_LIBRARY " +
            "WHERE BOOK_ID = :book_id" +
            "); ",
        mapSqlParameterSource, Boolean.class
    );
  }

  @Override
  public int deleteBook(Long bookId)
  {

    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("book_id", bookId);
    return namedParameterJdbcTemplate.update(
        "DELETE " +
            "FROM TBL_LIBRARY " +
            "WHERE BOOK_ID = :book_id; ",
        mapSqlParameterSource);
  }


  @Override
  public Integer numberOfLibraries(Long bookId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("book_id", bookId);

    return namedParameterJdbcTemplate.queryForObject(
        "SELECT COUNT(BOOK_ID) " +
            "FROM TBL_LIBRARY " +
            "WHERE BOOK_ID = :book_id",
        mapSqlParameterSource, Integer.class);
  }


  @Override
  public Boolean isInLibrary(Long userId, Long bookId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("b_id", bookId)
        .addValue("u_id", userId);

    return namedParameterJdbcTemplate.queryForObject(
        "SELECT EXISTS( " +
            "SELECT BOOK_ID, USER_ID " +
            "FROM TBL_LIBRARY " +
            "WHERE BOOK_ID = :b_id " +
            "AND USER_ID = :u_id);",
        mapSqlParameterSource, Boolean.class);
  }

}
