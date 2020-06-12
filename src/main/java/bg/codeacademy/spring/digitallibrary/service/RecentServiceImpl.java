package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.dto.BookDtoRecent;
import bg.codeacademy.spring.digitallibrary.model.Book;
import bg.codeacademy.spring.digitallibrary.repository.RecentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecentServiceImpl implements RecentService
{
  private final RecentRepository recentRepository;
  private final RatingServiceImpl ratingRepository;

  public RecentServiceImpl(RecentRepository recentRepository, RatingServiceImpl ratingRepository)
  {
    this.recentRepository = recentRepository;
    this.ratingRepository = ratingRepository;
  }

  @Override
  public void addToRecentList(Long userId, Long bookId){
    if (!recentRepository.containsBook(bookId)) {
      recentRepository.lastRed(userId, bookId);
    }
  }

  @Override
  public List<BookDtoRecent> findRecentRed(Long userId){
    List<Book> recent = recentRepository.findLastRed(userId);
    List<BookDtoRecent> dtoBooks = new ArrayList<>();
    for (Book book : recent) {

      BookDtoRecent bookDto = new BookDtoRecent()
          .setId(book.getId())
          .setTitle(book.getTitle())
          .setAuthor(book.getAuthor())
          .setGenre(book.getGenre())
          .setRating(ratingRepository.getAveRating(book.getId()))
          .setDate(recentRepository.findReadDate(userId, book.getId()));
      dtoBooks.add(bookDto);
    }
    return dtoBooks;
  }

  @Override
  public void deleteBookFromRecent(Long bookId)
  {
    if(recentRepository.containsBook(bookId)){
      recentRepository.deleteBook(bookId);
    }
  }
}
