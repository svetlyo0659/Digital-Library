package bg.codeacademy.spring.digitallibrary.controller;

import bg.codeacademy.spring.digitallibrary.dto.BookDto;
import bg.codeacademy.spring.digitallibrary.dto.BookDtoRecent;
import bg.codeacademy.spring.digitallibrary.model.Book;
import bg.codeacademy.spring.digitallibrary.model.User;
import bg.codeacademy.spring.digitallibrary.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/library")
public class LibraryController
{

  private final UserServiceImpl    userService;
  private final BookServiceImpl    bookService;
  private final LibraryServiceImpl libraryService;
  private final RatingServiceImpl  ratingService;
  private final RecentServiceImpl  recentService;


  @Autowired
  public LibraryController(UserServiceImpl userService,
                           BookServiceImpl bookService,
                           LibraryServiceImpl libraryService,
                           RatingServiceImpl ratingService,
                           RecentServiceImpl recentService)
  {
    this.bookService = bookService;
    this.libraryService = libraryService;
    this.userService = userService;
    this.ratingService = ratingService;
    this.recentService = recentService;
  }

  @GetMapping()
  public ResponseEntity<?> getBooksFromUserLibrary(@RequestParam(
      value = "orderCriteria", required = false, defaultValue = " '' ")
                                                       String orderCriteria,
                                                   Principal p)
  {
    Optional<User> currentUser = userService.findUserByUsername(p.getName());

    if (currentUser.isPresent() && currentUser.get().getEnabled() == true) {
      List<BookDto> bookDtoList = libraryService.findAllBooks(currentUser.get(), orderCriteria);
      if (!bookDtoList.isEmpty()) {
        return ResponseEntity.ok(bookDtoList);
      }
      return ResponseEntity.ok("No books in this library.");  // was 400 changed it to 200
    }
    return ResponseEntity.badRequest().body("Your account is temporary disabled");
  }

  @PostMapping("/add")
  public ResponseEntity<?> addBookToUserLibrary(@RequestParam(value = "bookId") Long bookId,
                                                Principal principal)
  {

    Optional<Book> bookToAdd = bookService.findById(bookId);
    Optional<User> currentUser = userService.findUserByUsername(principal.getName());

    if (currentUser.isPresent() && currentUser.get().getEnabled() == true) {
      if (bookToAdd.isPresent()) {
        Long currentUserId = currentUser.get().getId();
        libraryService.addBookToLibrary(currentUserId, bookId);
        ratingService.rate(currentUserId, bookId, 0.0);
        return ResponseEntity.ok("Book added");
      }
      return ResponseEntity.badRequest().body("Book book is already in your library");
    }
    return ResponseEntity.badRequest().body("Your account is temporary disabled");
  }

  @DeleteMapping("/remove")
  public ResponseEntity<?> removeBookFromUserLibrary(@RequestParam(value = "bookId") Long bookId,
                                                     Principal principal)
  {

    Optional<User> currentUser = userService.findUserByUsername(principal.getName());
    Optional<Book> bookToRemove = bookService.findById(bookId);

    if (currentUser.isPresent() && currentUser.get().getEnabled() && bookToRemove.isPresent()) {
      libraryService.deleteBookFromLibrary(currentUser.get().getId(), bookId);
      return ResponseEntity.ok("Book deleted");
    }
    return ResponseEntity.ok("Book not deleted");
  }

  @GetMapping("/download/{fileName:.+}")
  public ResponseEntity<?> downloadBook(@PathVariable String fileName,
                                        HttpServletRequest request,
                                        Principal p) throws IOException
  {
    Resource resource = bookService.download(fileName);
    Optional<Book> book = bookService.findByTitle(fileName);
    if (book.isPresent()) {
      if (book.get().getEnabled() == true) {
        String contentType = null;
        try {
          contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }
        catch (IOException ex) {

        }
        if (contentType == null) {
          contentType = "application/octet-stream";
        }
        Optional<User> currentUser = userService.findUserByUsername(p.getName());
        if (currentUser.isPresent() && currentUser.get().getEnabled() == true) {
          recentService.addToRecentList(currentUser.get().getId(),
              book.get().getId());

          return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(contentType))
              .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
              .body(resource);
        }
        return ResponseEntity.badRequest().body("This book is temporary unavailable for read/download!");
      }
      return ResponseEntity.badRequest().body("This book is not present");
    }
    return ResponseEntity.badRequest().body("Bad book name");
  }


  @GetMapping("/numberOfLibraries")
  public ResponseEntity<?> numberOfLibraries(@RequestParam(value = "bookId") Long bookId)
  {
    /// check
    return ResponseEntity.ok(String.format("This book is added in %s libraries",
        libraryService.numberOfLibraries(bookId)));
  }

  @PatchMapping("/{bookId}/rate")
  public ResponseEntity<?> rateBook(@PathVariable(value = "bookId", required = true) Long bookId,
                                    @RequestParam(value = "rating", required = true, defaultValue = "0.0") Double rating,
                                    Principal p)
  {
    Optional<User> currentUser = userService.findUserByUsername(p.getName());
    if (currentUser.isPresent() && currentUser.get().getEnabled() == true) {
      Long userId = currentUser.get().getId();
      Optional<Book> book = bookService.findById(bookId);
      if (book.isPresent()) {
        Optional<Double> yourRating = ratingService.getYourRating(userId, bookId);
        if (libraryService.isInLibrary(userId, bookId)) {
          if (!yourRating.isPresent() || yourRating.get() == 0) {
            ratingService.rate(userId, bookId, rating);
            Double newRating = ratingService.getAveRating(bookId); // or  null !
            return ResponseEntity.ok("New book rating is : " + newRating);
          }
          return ResponseEntity.badRequest().body("You already gave the book a score!");
        }
        return ResponseEntity.badRequest().body("This book is not in your library!");
      }
      return ResponseEntity.badRequest().body("Book not present!");
    }
    return ResponseEntity.badRequest().body("This user is temporary disabled!");
  }

  @GetMapping("/recent")
  public ResponseEntity<?> getRecentBooks(Principal p)
  {
    Optional<User> currentUser = userService.findUserByUsername(p.getName());
    if (currentUser.isPresent() && currentUser.get().getEnabled() == true) {
      List<BookDtoRecent> recentList = recentService.findRecentRed(currentUser.get().getId());
      if (!recentList.isEmpty()) {
        return ResponseEntity.ok(recentList);
      }
      return ResponseEntity.badRequest().body("no books found");
    }
    return ResponseEntity.badRequest().body("Your account is temporary disabled");
  }
}
