package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.dto.BookDto;
import bg.codeacademy.spring.digitallibrary.model.Book;
import bg.codeacademy.spring.digitallibrary.model.User;
import bg.codeacademy.spring.digitallibrary.repository.LibraryRepository;
import bg.codeacademy.spring.digitallibrary.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService
{
  private final LibraryRepository libraryRepository;
  private final RatingRepository  ratingRepository;

  @Autowired
  public LibraryServiceImpl(LibraryRepository libraryRepository, RatingRepository ratingRepository)
  {
    this.libraryRepository = libraryRepository;
    this.ratingRepository = ratingRepository;
  }

  @Override
  public void addBookToLibrary(Long user_id, Long book_id)
  {
    libraryRepository.addBookToLibrary(user_id, book_id);
  }

  @Override
  public void deleteBookFromLibrary(Long user_id, Long book_id)
  {
    libraryRepository.deleteBookFromLibrary(user_id, book_id);
  }

  @Override
  public List<BookDto> findAllBooks(User user, String orderCriteria)
  {
    List<Book> userBooks = libraryRepository.findAllBooksInLibrary(user.getId(), orderCriteria);
    List<BookDto> userDtoBooks = new ArrayList<>();

    if (!userBooks.isEmpty()) {

      for (Book book : userBooks) {

        String Url;
        if (book.getEnabled().equals(true)) {
          Url = ServletUriComponentsBuilder.fromCurrentContextPath()
              .path("/api/v1/library/download/") /// check this
              .path(book.getTitle()).toUriString();
        }
        else {
          Url = "N/A for download";
        }

        BookDto bookDto = new BookDto()
            .setId(book.getId())
            .setAuthor(book.getAuthor())
            .setGenre(book.getGenre())
            .setRating(ratingRepository.getAveRating(book.getId()))
            .setTitle(book.getTitle())
            .setUrl(Url);
        userDtoBooks.add(bookDto);
      }
    }

    return userDtoBooks;
  }

  @Override
  public Integer numberOfLibraries(Long bookId)
  {
    return libraryRepository.numberOfLibraries(bookId);
  }


  @Override
  public Boolean isInLibrary(Long userId, Long bookId)
  {
    return libraryRepository.isInLibrary(userId, bookId);
  }


}
