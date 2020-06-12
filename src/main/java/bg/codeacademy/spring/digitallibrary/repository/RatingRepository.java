package bg.codeacademy.spring.digitallibrary.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RatingRepository implements RatingDao
{
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public RatingRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public Double getAveRating(Long bookId)
  {

    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("book_id", bookId);

    Double rating;
    rating =  namedParameterJdbcTemplate.queryForObject(
        "SELECT AVG(RATING) " +
            "FROM TBL_RATING " +
            "WHERE BOOK_ID = :book_id " +
            "AND RATING > 0;"
        , mapSqlParameterSource, Double.class);
    if(rating == null){
      rating =  0.0;
    }
    return rating;
  }

  @Override
  public void rate(Long userId, Long bookId, Double rating)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("user_id", userId);
    mapSqlParameterSource.addValue("book_id", bookId);
    mapSqlParameterSource.addValue("rating", rating);
    namedParameterJdbcTemplate.update(
        "INSERT INTO TBL_RATING (USER_ID, BOOK_ID, RATING) VALUES(:user_id, :book_id,:rating)",
        mapSqlParameterSource);

  }

  @Override
  public Double getYourRating(Long userId, Long bookId)
  {

    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("user_id", userId);
    mapSqlParameterSource.addValue("book_id", bookId);

    try {
      return  namedParameterJdbcTemplate.queryForObject(
          "SELECT RATING " +
              "FROM TBL_RATING " +
              "WHERE USER_ID = :user_id AND BOOK_ID = :book_id",
          mapSqlParameterSource, Double.class);
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Override
  public Boolean containsUser(Long userId){
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", userId);
    return namedParameterJdbcTemplate.queryForObject(
        "SELECT EXISTS(" +
            "SELECT USER_ID " +
            "FROM TBL_RATING " +
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
            "FROM TBL_RATING " +
            "WHERE USER_ID = :user_id; ",
        mapSqlParameterSource);
  }
  @Override
  public Boolean containsBook(Long bookId){
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("book_id", bookId);
    return namedParameterJdbcTemplate.queryForObject(
        "SELECT EXISTS(" +
            "SELECT BOOK_ID " +
            "FROM TBL_RATING " +
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
            "FROM TBL_RATING " +
            "WHERE BOOK_ID = :book_id; ",
        mapSqlParameterSource);
  }

}
