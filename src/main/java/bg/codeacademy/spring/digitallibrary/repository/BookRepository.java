package bg.codeacademy.spring.digitallibrary.repository;


import bg.codeacademy.spring.digitallibrary.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository implements BookDao
{

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public BookRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }


  @Override
  public Long save(Book book)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("title", book.getTitle());
    mapSqlParameterSource.addValue("genre", book.getGenre());
    mapSqlParameterSource.addValue("author", book.getAuthor());
    mapSqlParameterSource.addValue("enable", book.getEnabled());

    KeyHolder holder = new GeneratedKeyHolder();

    namedParameterJdbcTemplate.update(
        "INSERT INTO TBL_BOOKS (TITLE, GENRE, AUTHOR, ISENABLED) " +
            "VALUES(:title, :genre, :author, :enable)",
        mapSqlParameterSource, holder, new String[]{"BOOK_ID"});
    Long b_id = holder.getKey().longValue();
    return b_id;
  }

  @Override
  public Boolean containsBook(Long bookId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("book_id", bookId);
    return namedParameterJdbcTemplate.queryForObject(
        "SELECT EXISTS(" +
            "SELECT BOOK_ID " +
            "FROM TBL_BOOKS " +
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
            "FROM TBL_BOOKS " +
            "WHERE BOOK_ID = :book_id; ",
        mapSqlParameterSource);
  }

  @Override
  public List<Book> findAllBooks(String title, String author, String token)
  {

    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("title", "%" + title + "%");
    mapSqlParameterSource.addValue("author", "%" + author + "%");
    mapSqlParameterSource.addValue("token", "%" + token + "%");

    return namedParameterJdbcTemplate.query(
        "SELECT * FROM TBL_BOOKS  " +
            "WHERE TITLE LIKE :title " +
            "AND AUTHOR LIKE :author " +
            "AND ISENABLED LIKE :token",
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
  public Book findById(Long bookId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("b_id", bookId);
    try {
      return namedParameterJdbcTemplate.queryForObject(
          "SELECT * " +
              "FROM TBL_BOOKS " +
              "WHERE BOOK_ID = :b_id",
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
    catch (DataAccessException e) {
      return null;
    }


  }

  @Override
  public Book findByTitle(String title)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("title", title);

    try {
      return namedParameterJdbcTemplate.queryForObject(
          "SELECT * " +
              "FROM TBL_BOOKS " +
              "WHERE TITLE = :title",
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
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  public void updateStatus(Long bookId, Boolean enable)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("book_id", bookId);
    mapSqlParameterSource.addValue("isEnabled", enable);
    namedParameterJdbcTemplate.update(
        "UPDATE TBL_BOOKS " +
            "SET ISENABLED = :isEnabled " +
            "WHERE Book_ID = :book_id",
        mapSqlParameterSource);
  }

}
