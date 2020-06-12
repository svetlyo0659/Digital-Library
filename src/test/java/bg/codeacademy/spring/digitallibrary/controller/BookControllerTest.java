package bg.codeacademy.spring.digitallibrary.controller;

import bg.codeacademy.spring.digitallibrary.DigitalLibraryApplication;
import bg.codeacademy.spring.digitallibrary.model.Book;
import bg.codeacademy.spring.digitallibrary.model.User;
import bg.codeacademy.spring.digitallibrary.service.*;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = DigitalLibraryApplication.class)
// Extend AbstractTestNGSpringContextTests to use TestNG tests in Spring Boot
public class BookControllerTest extends AbstractTestNGSpringContextTests
{

  @LocalServerPort
  private int port;


  @BeforeClass
  private void getConnection()
  {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setUrl("jdbc:h2:file:./digital_library");
    dataSource.setUsername("sa");
    dataSource.setPassword("password");
    dataSource.setDriverClass(org.h2.Driver.class);
  }

  @BeforeClass(alwaysRun = true, dependsOnMethods = "springTestContextPrepareTestInstance")
  protected void setupRestAssured()
  {
    RestAssured.port = port;
  }


  @Autowired
  private BookServiceImpl   bookService;
  @Autowired
  private UserServiceImpl   userService;
  @Autowired
  private RatingServiceImpl ratingService;

  @Test
  public void testAddBook()
  {
    User user = new User()
        .setUsername("testUsername1")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("AUTHOR")
        .setEnabled(true);

    userService.save(user);

    RestAssured
        .given()
        .auth()
        .basic("testUsername1", "123456")
        .and()
        .multiPart("file", new File("C:/Users/Administrator/Desktop/" +
            "BOOKS FOR JAVA !!!!/Snippets1745188125-1.pdf")) /// file from local machine
        .param("genre", "IT")
        .when()
        .post("/api/v1/book")
        .then()
        .assertThat()
        .statusCode(200);

    Book book = bookService.findByTitle("Snippets1745188125-1.pdf").get();
    try {
      ratingService.deleteRating(book.getId());
      bookService.deleteBook(book.getId());
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user.getUsername());
  }

  @Test
  public void testGetAllBookByTitle()
  {

    User user = new User()
        .setUsername("testUsername")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("AUTHOR")
        .setEnabled(true);

    userService.save(user);

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("TEST TITLE");

    // save the foo book
    bookService.saveBook(book.getTitle(),book.getGenre(),book.getAuthor());


    RestAssured
        .given()
        .auth()
        .basic("testUsername", "123456")
        .and()
        .param("title", "TEST TITLE")
        .param("author","")
        .when()
        .get("/api/v1/book")
        .then()
        .assertThat()
        .statusCode(200);


    Book book1 = bookService.findByTitle("TEST TITLE").get();
    try {
      bookService.deleteBook(book1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user.getUsername()); // remove the user from the DB;

  }

  @Test
  public void testGetAllBookByAuthor()
  {

    User user = new User()
        .setUsername("testUsername")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("AUTHOR")
        .setEnabled(true);

    userService.save(user);

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("TEST TITLE");

    // save the foo book
    bookService.saveBook(book.getTitle(),book.getGenre(),book.getAuthor());


    RestAssured
        .given()
        .auth()
        .basic("testUsername", "123456")
        .and()
        .param("title", "")
        .param("author","TEST AUTHOR")
        .when()
        .get("/api/v1/book")
        .then()
        .assertThat()
        .statusCode(200);


    Book book1 = bookService.findByTitle("TEST TITLE").get();
    try {
      bookService.deleteBook(book1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user.getUsername()); // remove the user from the DB;

  }

  @Test
  public void testInvalidEnableBook()
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

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("TEST TITLE");

    // save the foo book
    bookService.saveBook(book.getTitle(),book.getGenre(),book.getAuthor());

    Book b1 = bookService.findByTitle("TEST TITLE").get();

    RestAssured
        .given()
        .auth()
        .basic("testUsername", "123456")
        .and()
        .pathParam("bookId", b1.getId()) // bookID
        .param("enable", false)
        .when()
        .patch("/api/v1/book/{bookId}/enable")
        .then()
        .assertThat()
        .statusCode(400);

    try {
      bookService.deleteBook(b1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user.getUsername()); // remove the user from the DB;

  }

  @Test
  public void testValidEnableBook()
  {
    /// create FOO user
    User user = new User()
        .setUsername("testUsername")
        .setName("TEST AUTHOR")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("AUTHOR")
        .setEnabled(true);

    /// SAVE THE FOO USER
    userService.save(user);

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("TEST TITLE");

    // save the foo book
    bookService.saveBook(book.getTitle(),book.getGenre(),book.getAuthor());

    Book b1 = bookService.findByTitle("TEST TITLE").get();

    RestAssured
        .given()
        .auth()
        .basic("testUsername", "123456")
        .and()
        .pathParam("bookId", b1.getId()) // bookID
        .param("enable", false)
        .when()
        .patch("/api/v1/book/{bookId}/enable")
        .then()
        .assertThat()
        .statusCode(200);

    try {
      bookService.deleteBook(b1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user.getUsername()); // remove the user from the DB;

  }


}
