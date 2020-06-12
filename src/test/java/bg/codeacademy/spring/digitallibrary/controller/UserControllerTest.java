package bg.codeacademy.spring.digitallibrary.controller;

import bg.codeacademy.spring.digitallibrary.DigitalLibraryApplication;
import bg.codeacademy.spring.digitallibrary.model.User;
import bg.codeacademy.spring.digitallibrary.service.UserServiceImpl;
import io.restassured.RestAssured;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = DigitalLibraryApplication.class)
// Extend AbstractTestNGSpringContextTests to use TestNG tests in Spring Boot
public class UserControllerTest extends AbstractTestNGSpringContextTests
{

  @Autowired
  private UserServiceImpl userService;


  @LocalServerPort
  private int port;


 //@BeforeClass
 //private void getConnection()
 //{
 //  SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
 //  dataSource.setUrl("jdbc:h2:file:./digital_library");
 //  dataSource.setUsername("sa");
 //  dataSource.setPassword("password");
 //  dataSource.setDriverClass(org.h2.Driver.class);
 //}

  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;
  }

  @BeforeMethod
  public void createUsers()
  {
    User admin = userService.findUserByUsername("admin").get();

  }

  //@Test(dataProvider = "jdbc:h2:file:./digital_library")
  @Test
  public void testCreateUser()
  {
    User user = new User()
        .setUsername("testUsername")
        .setName("Full name")
        .setPassword("123456")
        .setRole("AUTHOR")
        .setEnabled(true);
    given()
        .param("username", user.getUsername())
        .param("name", user.getName())
        .param("password", user.getPassword())
        .param("role", user.getRole())
        .param("isEnabled", user.getEnabled())
        .when()
        .post("/api/v1/user")
        .then()
        .assertThat()
        .statusCode(200);

    // delete the user
    userService.deleteByUsername(user.getUsername());
  }

  @Test
  public void testCreateInvalidUser() ///400 if the username is already taken
  {
    User user1 = new User()
        .setUsername("testUsername")
        .setName("Full name")
        .setPassword("123456")
        .setRole("AUTHOR")
        .setEnabled(true);
    userService.save(user1);

    User user = new User()
        .setUsername("testUsername")
        .setName("Full name")
        .setPassword("123456")
        .setRole("AUTHOR")
        .setEnabled(true);
    given()
        .param("username", user.getUsername())
        .param("name", user.getName())
        .param("password", user.getPassword())
        .param("role", user.getRole())
        .param("isEnabled", user.getEnabled())
        .when()
        .post("/api/v1/user")
        .then()
        .assertThat()
        .statusCode(400);

    // delete the user
    userService.deleteByUsername(user1.getUsername());
  }

  @Test
  public void testRemoveUser()
  {

    /// create FOO user
    User user = new User()
        .setUsername("testUsername")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("AUTHOR")
        .setEnabled(true);

    /// SAVE THE FOO USER
    userService.save(user);

    /// get the user to get the auto_incremented ID
    User u = userService.findUserByUsername("testUsername").get();

    RestAssured
        .given()
        .auth()
        .basic("admin", "123456")
        .and()
        .pathParam("userId", u.getId())
        .when()
        .delete("/api/v1/user/{userId}/remove")
        .then()
        .assertThat()
        .statusCode(200);

    // delete the user from the DB
    userService.deleteByUsername(user.getUsername());
  }

  @Test
  public void testEnableUser()
  {
    /// create FOO user
    User user = new User()
        .setUsername("testUsername")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("AUTHOR")
        .setEnabled(true);

    /// SAVE THE FOO USER
    userService.save(user);

    /// get the user to get the auto_incremented ID
    User u = userService.findUserByUsername("testUsername").get();

    RestAssured
        .given()
        .auth()
        .basic("admin", "123456")
        .and()
        .pathParam("userId", u.getId())
        .param("enable", false)
        .when()
        .patch("/api/v1/user/{userId}/enable")
        .then()
        .assertThat()
        .statusCode(200);

    // delete the user from the DB
    userService.deleteByUsername(user.getUsername());

  }

  @Test
  public void testInvalidEnableUser()
  {

    RestAssured
        .given()
        .auth()
        .basic("admin", "123456")
        .and()
        .pathParam("userId", 1000000L) // this is not OK
        .param("enable", false)
        .when()
        .patch("/api/v1/user/{userId}/enable")
        .then()
        .assertThat()
        .statusCode(400);
  }


  @Test
  public void testGetAllUsers()
  {

    /// create FOO user
    User user = new User()
        .setUsername("Admin1")
        .setName("ADMIN ADMIN")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("ADMIN")
        .setEnabled(true);

    /// SAVE THE FOO USER
    userService.save(user);

    /// get the user to get the auto_incremented ID
    User u = userService.findUserByUsername("Admin1").get();

    RestAssured
        .given()
        .auth()
        .basic("Admin1", "123456")
        .and()
        .when()
        .get("/api/v1/user")
        .then()
        .assertThat()
        .statusCode(200);

    // delete the user from the DB
    userService.deleteByUsername(user.getUsername());
  }


}
