package bg.codeacademy.spring.digitallibrary.repository;

import bg.codeacademy.spring.digitallibrary.model.Book;

import java.util.List;

public interface BookDao
{
  List<Book> findAllBooks(String title, String author, String token);

  Book findById(Long bookId);

  Long save(Book book);

  Boolean containsBook(Long bookId);

  int deleteBook(Long bookId);

  Book findByTitle(String title);

  void updateStatus(Long bookId, Boolean enable);
}
