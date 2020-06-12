package bg.codeacademy.spring.digitallibrary.repository;

import bg.codeacademy.spring.digitallibrary.model.Book;

import java.time.LocalDateTime;
import java.util.List;

public interface RecentDao
{
  void lastRed(Long userId, Long bookId);

  List<Book> findLastRed(Long userId);

  Boolean containsUser(Long userId);

  int deleteUser(Long userId);

  Boolean containsBook(Long bookId);

  int deleteBook(Long bookId);

  LocalDateTime findReadDate(Long userId, Long bookId);
}
