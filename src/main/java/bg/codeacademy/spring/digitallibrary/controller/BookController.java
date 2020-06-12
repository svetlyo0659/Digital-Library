package bg.codeacademy.spring.digitallibrary.controller;

import bg.codeacademy.spring.digitallibrary.dto.BookDtoNoUrl;
import bg.codeacademy.spring.digitallibrary.dto.BookDtoWithStatus;
import bg.codeacademy.spring.digitallibrary.model.Book;
import bg.codeacademy.spring.digitallibrary.model.User;
import bg.codeacademy.spring.digitallibrary.service.BookServiceImpl;
import bg.codeacademy.spring.digitallibrary.service.RatingServiceImpl;
import bg.codeacademy.spring.digitallibrary.service.UserServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@RestController
@RequestMapping("/api/v1/book")
public class BookController
{

  private final BookServiceImpl   bookService;
  private final UserServiceImpl   userService;
  private final RatingServiceImpl ratingService;

  @Autowired
  public BookController(BookServiceImpl bookService,
                        UserServiceImpl userService,
                        RatingServiceImpl ratingService)
  {
    this.bookService = bookService;
    this.userService = userService;
    this.ratingService = ratingService;
  }

  @PostMapping()
  public ResponseEntity<?> addBook(@RequestParam(value = "file", required = true) MultipartFile file,
                                   @RequestParam(value = "genre", required = true) String genre,
                                   Principal p) throws IOException

  {
    Optional<User> currentUser = userService.findUserByUsername(p.getName());
    String fileName = bookService.uploadFile(file);

    if (currentUser.isPresent() && currentUser.get().getEnabled() == true && !bookService.findByTitle(fileName).isPresent()) {

      BookDtoNoUrl bookDto = bookService.saveBook(fileName, genre, currentUser.get().getName());
      Book b1 = bookService.findByTitle(fileName).get();
      ratingService.rate(currentUser.get().getId(), b1.getId(), 0.0);
      return ResponseEntity.ok(bookDto);

    }
    return ResponseEntity.badRequest().body("This book already exists!");
  }

  @DeleteMapping("/{id}/remove")
  public ResponseEntity<?> deleteBook(@PathVariable Long id) throws IOException
  {
    if (bookService.findById(id).isPresent()) {
      bookService.deleteBook(id);
      return ResponseEntity.ok("Book was deleted");
    }
    return ResponseEntity.badRequest().body("Your account is temporary disabled");
  }

  @GetMapping()
  public ResponseEntity<?> findAll(@RequestParam(value = "title", required = false, defaultValue = "")
                                       String title,
                                   @RequestParam(value = "author", required = false, defaultValue = "")
                                       String author,
                                   Principal p)
  {
    Optional<User> currentUser = userService.findUserByUsername(p.getName());
    if (currentUser.isPresent() && currentUser.get().getEnabled() == true) {
      if (currentUser.get().getRole().equalsIgnoreCase("AUTHOR")) {
        List<BookDtoWithStatus> allBooks = bookService.findAllBooks(title, author);
        if (!allBooks.isEmpty()) {
          return ResponseEntity.ok(allBooks);
        }
      }
      if (currentUser.get().getRole().equalsIgnoreCase("READER")) {
        List<BookDtoNoUrl> allBooks = bookService.findEnabledBooks(title, author);
        if (!allBooks.isEmpty()) {
          return ResponseEntity.ok(allBooks);
        }
      }
      return ResponseEntity.badRequest().body("No books were found.");
    }
    return ResponseEntity.badRequest().body("Your account is temporary disabled");
  }

  @PatchMapping("/{bookId}/enable")
  public ResponseEntity<?> enableBook(@PathVariable(value = "bookId", required = true) Long bookId,
                                      @RequestParam(value = "enable", required = true) Boolean enable,
                                      Principal principal)
  {
    Optional<User> currentUser = userService.findUserByUsername(principal.getName());
    if (currentUser.isPresent() && currentUser.get().getEnabled() == true) {
      Optional<Book> book = bookService.findById(bookId);

      //check if the book is added by te current user
      if (book.isPresent() && book.get().getAuthor().equals(currentUser.get().getName())) {
        bookService.updateStatus(bookId, enable);
        return ResponseEntity.ok("Enable book set to: " + enable);
      }
      return ResponseEntity.badRequest().body("This book is added by another user!");
    }
    return ResponseEntity.badRequest().build();
  }

}


