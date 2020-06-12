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

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = DigitalLibraryApplication.class)
// Extend AbstractTestNGSpringContextTests to use TestNG tests in Spring Boot
public class LibraryControllerTest extends AbstractTestNGSpringContextTests
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
  private BookServiceImpl    bookService;
  @Autowired
  private UserServiceImpl    userService;
  @Autowired
  private RatingServiceImpl  ratingService;
  @Autowired
  private RecentServiceImpl  recentService;
  @Autowired
  private LibraryServiceImpl libraryService;


  @Test
  public void testAddBookToLibrary()
  {

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("TEST TITLE");

    // save the foo book
    bookService.saveBook(book.getTitle(), book.getGenre(), book.getAuthor());

    User user1 = new User()
        .setUsername("testReader")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("READER")
        .setEnabled(true);

    userService.save(user1);

    Book book1 = bookService.findByTitle("TEST TITLE").get();

    RestAssured
        .given()
        .auth()
        .basic("testReader", "123456")
        .and()
        .param("bookId", book1.getId())
        .when()
        .post("/api/v1/library/add")
        .then()
        .assertThat()
        .statusCode(200);

    User user2 = userService.findUserByUsername("testReader").get(); // get the current user form the DB

    ratingService.deleteRating(book1.getId());
    libraryService.deleteBookFromLibrary(user2.getId(), book1.getId());
    try {
      bookService.deleteBook(book1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user2.getUsername());
  }


  @Test
  public void testRemoveBookFromLibrary(){

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("TEST TITLE");

    // save the foo book
    bookService.saveBook(book.getTitle(), book.getGenre(), book.getAuthor());

    User user1 = new User()
        .setUsername("testReader")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("READER")
        .setEnabled(true);

    userService.save(user1);

    Book book1 = bookService.findByTitle("TEST TITLE").get(); // get the book from the DB
    User user2 = userService.findUserByUsername("testReader").get(); // get the current user form the DB

    libraryService.addBookToLibrary(user2.getId(),book1.getId());

    RestAssured
        .given()
        .auth()
        .basic("testReader", "123456")
        .and()
        .param("bookId", book1.getId())
        .when()
        .delete("/api/v1/library/remove")
        .then()
        .assertThat()
        .statusCode(200);

    try {
      bookService.deleteBook(book1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user2.getUsername()); // remove the user form the DB

  }

  @Test
  public void testGetBooksFromUserLibrary(){

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("TEST TITLE");

    // save the foo book
    bookService.saveBook(book.getTitle(), book.getGenre(), book.getAuthor());

    User user1 = new User()
        .setUsername("testReader")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("READER")
        .setEnabled(true);

    userService.save(user1);

    Book book1 = bookService.findByTitle("TEST TITLE").get(); // get the book from the DB
    User user2 = userService.findUserByUsername("testReader").get(); // get the current user form the DB

    libraryService.addBookToLibrary(user2.getId(),book1.getId());

    RestAssured
        .given()
        .auth()
        .basic("testReader", "123456")
        .and()
        .param("orderCriteria", " '' ")
        .when()
        .get("/api/v1/library")
        .then()
        .assertThat()
        .statusCode(200);

    libraryService.deleteBookFromLibrary(user2.getId(), book1.getId());
    try {
      bookService.deleteBook(book1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user2.getUsername()); // remove the user form the DB

  }

  @Test
  public  void testRateBookFromUserLibrary(){

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("TEST TITLE");

    // save the foo book
    bookService.saveBook(book.getTitle(), book.getGenre(), book.getAuthor());

    User user1 = new User()
        .setUsername("testReader")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("READER")
        .setEnabled(true);

    userService.save(user1);

    Book book1 = bookService.findByTitle("TEST TITLE").get(); // get the book from the DB
    User user2 = userService.findUserByUsername("testReader").get(); // get the current user form the DB

    libraryService.addBookToLibrary(user2.getId(),book1.getId());
    ratingService.rate(user2.getId(),book1.getId(),0.0);

    RestAssured
        .given()
        .auth()
        .basic("testReader", "123456")
        .and()
        .pathParam("bookId",book1.getId())
        .param("rating",4)
        .when()
        .patch("/api/v1/library/{bookId}/rate")
        .then()
        .assertThat()
        .statusCode(200);

    ratingService.deleteRating(book1.getId());
    libraryService.deleteBookFromLibrary(user2.getId(), book1.getId());
    try {
      bookService.deleteBook(book1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user2.getUsername());

  }


  @Test
  public void testDownloadBook(){

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("testDownloadBook.pdf");

    // save the foo book
    bookService.saveBook(book.getTitle(), book.getGenre(), book.getAuthor());

    User user1 = new User()
        .setUsername("testReader")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("READER")
        .setEnabled(true);

    userService.save(user1);

    Book book1 = bookService.findByTitle("testDownloadBook.pdf").get(); // get the book from the DB
    User user2 = userService.findUserByUsername("testReader").get(); // get the current user form the DB

    libraryService.addBookToLibrary(user2.getId(),book1.getId());

    RestAssured
        .given()
        .auth()
        .basic("testReader", "123456")
        .and()
        .pathParam("fileName:.+","testDownloadBook.pdf")
        .when()
        .get("/api/v1/library/download/{fileName:.+}")
        .then()
        .assertThat()
        .statusCode(200);

    recentService.deleteBookFromRecent(book1.getId());
    libraryService.deleteBookFromLibrary(user2.getId(), book1.getId());
    try {
      bookService.deleteBook(book1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user2.getUsername());

  }

  @Test
  public void testGetRecentBooks(){

    // create foo book
    Book book = new Book()
        .setAuthor("TEST AUTHOR")
        .setGenre("TEST GENRE")
        .setEnabled(true)
        .setTitle("testGetRecentBooks.pdf");

    // save the foo book
    bookService.saveBook(book.getTitle(), book.getGenre(), book.getAuthor());

    User user1 = new User()
        .setUsername("testReader")
        .setName("Full name")
        .setPassword(new BCryptPasswordEncoder().encode("123456"))
        .setRole("READER")
        .setEnabled(true);

    userService.save(user1);

    Book book1 = bookService.findByTitle("testGetRecentBooks.pdf").get(); // get the book from the DB
    User user2 = userService.findUserByUsername("testReader").get(); // get the current user form the DB

    libraryService.addBookToLibrary(user2.getId(),book1.getId());
    recentService.addToRecentList(user2.getId(),book1.getId());

    RestAssured
        .given()
        .auth()
        .basic("testReader", "123456")
        .and()
        .when()
        .get("/api/v1/library/recent")
        .then()
        .assertThat()
        .statusCode(200);

    recentService.deleteBookFromRecent(book1.getId());
    libraryService.deleteBookFromLibrary(user2.getId(), book1.getId());
    try {
      bookService.deleteBook(book1.getId()); // remove the book from the DB
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    userService.deleteByUsername(user2.getUsername());

  }




}
