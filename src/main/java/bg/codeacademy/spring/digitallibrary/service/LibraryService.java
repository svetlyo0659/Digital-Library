package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.dto.BookDto;
import bg.codeacademy.spring.digitallibrary.model.Book;
import bg.codeacademy.spring.digitallibrary.model.User;

import java.security.Principal;
import java.util.List;

public interface LibraryService
{
  void addBookToLibrary(Long user_id, Long book_id);

  void deleteBookFromLibrary(Long user_id, Long book_id);

  List<BookDto> findAllBooks(User user, String orderCriteria);

  Integer numberOfLibraries(Long bookId);

  Boolean isInLibrary(Long userId, Long bookId);
}
