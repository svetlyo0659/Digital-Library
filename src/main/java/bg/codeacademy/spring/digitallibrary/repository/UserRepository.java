package bg.codeacademy.spring.digitallibrary.repository;

import bg.codeacademy.spring.digitallibrary.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository implements UserDao
{
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate)
  {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  //works
  @Override
  public List<User> findAllUsers()
  {
    return namedParameterJdbcTemplate.query(
        "SELECT * " +
            "FROM TBL_USERS; ",
        (rs, rowNum) ->
            new User().setId(rs.getLong("user_id"))
                .setUsername(rs.getString("username"))
                .setName(rs.getString("name"))
                .setPassword(rs.getString("password"))
                .setRole(rs.getString("role"))
                .setEnabled(rs.getBoolean("isEnabled"))
    );
  }


  // works
  @Override
  public int save(User user)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
    mapSqlParameterSource.addValue("username", user.getUsername());
    mapSqlParameterSource.addValue("name", user.getName());
    mapSqlParameterSource.addValue("password", user.getPassword());
    mapSqlParameterSource.addValue("role", user.getRole());
    mapSqlParameterSource.addValue("isEnabled", user.getEnabled());

    KeyHolder holder = new GeneratedKeyHolder();

    namedParameterJdbcTemplate.update(
        "INSERT INTO TBL_USERS (username, name, password, role, isEnabled) " +
            "VALUES(:username, :name, :password, :role, :isEnabled)",
        mapSqlParameterSource, holder, new String[]{"user_id"});
    return holder.getKey().intValue();
  }


  @Override
  public Boolean containsUser(Long userId)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", userId);
    return namedParameterJdbcTemplate.queryForObject(
        "SELECT EXISTS(" +
            "SELECT USER_ID " +
            "FROM TBL_USERS " +
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
            "FROM TBL_USERS " +
            "WHERE USER_ID = :user_id; ",
        mapSqlParameterSource);
  }

  @Override
  public User findUserById(Long userId)
  {

    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", userId);
    try {
      return namedParameterJdbcTemplate.queryForObject(
          "SELECT * FROM TBL_USERS WHERE user_id = :user_id",
          mapSqlParameterSource,
          (rs, rowNum) ->
              new User()
                  .setId(rs.getLong("user_id"))
                  .setUsername(rs.getString("username"))
                  .setName(rs.getString("name"))
                  .setPassword(rs.getString("password"))
                  .setRole(rs.getString("role"))
                  .setEnabled(rs.getBoolean("isEnabled"))
      );
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }
  }

  @Override
  public User findUserByUsername(String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("username", username);

    try {
      return namedParameterJdbcTemplate.queryForObject(
          "SELECT * " +
              "FROM TBL_USERS " +
              "WHERE username = :username", mapSqlParameterSource,
          (rs, rowNum) ->
              new User().setId(rs.getLong("user_id"))
                  .setUsername(rs.getString("username"))
                  .setName(rs.getString("name"))
                  .setPassword(rs.getString("password"))
                  .setRole(rs.getString("role"))
                  .setEnabled(rs.getBoolean("isEnabled"))
      );
    }
    catch (EmptyResultDataAccessException ex) {
      return null;
    }

  }

  @Override
  public void enableUser(Long userId,
                         Boolean isEnabled)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("isEnabled", isEnabled)
        .addValue("user_id", userId);
    namedParameterJdbcTemplate.update(
        "UPDATE TBL_USERS " +
            "SET isEnabled = :isEnabled " +
            "WHERE user_id = :user_id",
        mapSqlParameterSource);
  }

  /// used for tests
  public void deleteByUsername(String username)
  {
    MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
        .addValue("username", username);
    namedParameterJdbcTemplate.update(
        "DELETE " +
            "FROM TBL_USERS " +
            "WHERE username = :username",
        mapSqlParameterSource);
  }
}
