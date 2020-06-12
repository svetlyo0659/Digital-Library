package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.dto.BookDtoRecent;

import java.util.List;

public interface RecentService
{

  void addToRecentList(Long userId, Long bookId);

  List<BookDtoRecent> findRecentRed(Long userId);

  void deleteBookFromRecent(Long bookId);

}
