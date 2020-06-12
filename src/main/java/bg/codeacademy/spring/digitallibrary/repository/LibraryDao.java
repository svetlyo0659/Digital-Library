package bg.codeacademy.spring.digitallibrary.repository;

import bg.codeacademy.spring.digitallibrary.model.Book;

import java.util.List;

public interface LibraryDao
{
  void addBookToLibrary(Long userId, Long bookId);

  List<Book> findAllBooksInLibrary(Long userId, String criteria);

  void deleteBookFromLibrary(Long userId, Long bookId);

  int deleteUser(Long userId);

  Boolean containsBook(Long bookId);

  Boolean containsUser(Long userId);

  int deleteBook(Long bookId);

  Integer numberOfLibraries(Long bookId);

  Boolean isInLibrary(Long userId, Long bookId);
}
