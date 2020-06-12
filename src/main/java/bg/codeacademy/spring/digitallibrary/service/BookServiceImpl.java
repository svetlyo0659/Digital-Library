package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.config.FileStorageProperties;
import bg.codeacademy.spring.digitallibrary.dto.BookDtoNoUrl;
import bg.codeacademy.spring.digitallibrary.dto.BookDtoWithStatus;
import bg.codeacademy.spring.digitallibrary.exception.MyFileNotFoundException;
import bg.codeacademy.spring.digitallibrary.model.Book;
import bg.codeacademy.spring.digitallibrary.repository.BookRepository;
import bg.codeacademy.spring.digitallibrary.repository.LibraryRepository;
import bg.codeacademy.spring.digitallibrary.repository.RatingRepository;
import bg.codeacademy.spring.digitallibrary.repository.RecentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService
{
  private final BookRepository    bookRepository;
  private final Path              fileStorageLocation;
  private final LibraryRepository libraryRepository;
  private final RatingRepository  ratingRepository;
  private final RecentRepository  recentRepository;

  @Autowired
  public BookServiceImpl(FileStorageProperties fileStorageProperties,
                         BookRepository bookRepository, LibraryRepository libraryRepository, RatingRepository ratingRepository, RecentRepository recentRepository) throws IOException
  {
    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

    this.libraryRepository = libraryRepository;
    this.ratingRepository = ratingRepository;
    this.recentRepository = recentRepository;
    Files.createDirectories(this.fileStorageLocation);
    this.bookRepository = bookRepository;
  }

  @Override
  public String uploadFile(MultipartFile file) throws IOException
  {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    Path targetLocation = this.fileStorageLocation.resolve(fileName);
    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    return fileName;
  }

  @Override
  public BookDtoNoUrl saveBook(String title, String genre, String author)
  {
    Book book = new Book()
        .setTitle(title)
        .setGenre(genre)
        .setAuthor(author)
        .setEnabled(true);
    Long book_id = bookRepository.save(book);

    return new BookDtoNoUrl()
        .setId(book_id)
        .setTitle(title)
        .setGenre(genre)
        .setAuthor(author)
        .setRating(ratingRepository.getAveRating(book.getId()));
  }

  @Override
  public Resource download(String fileName) throws MalformedURLException
  {

    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      }
      else {
        throw new MyFileNotFoundException("File not found " + fileName);
      }
    }
    catch (MalformedURLException ex) {
      throw new MyFileNotFoundException("File not found " + fileName, ex);
    }
  }

  @Override
  public void deleteBook(Long book_id) throws IOException
  {
    String fileName = bookRepository.findById(book_id).getTitle();
    Path targetLocation = this.fileStorageLocation.resolve(fileName);
    Files.deleteIfExists(targetLocation);
    if (libraryRepository.containsBook(book_id)) {
      bookRepository.deleteBook(book_id);
    }
    if (ratingRepository.containsBook(book_id)) {
      ratingRepository.deleteBook(book_id);
    }
    if (recentRepository.containsBook(book_id)) {
      recentRepository.deleteBook(book_id);
    }
    if (bookRepository.containsBook(book_id)) {
      bookRepository.deleteBook(book_id);
    }
  }

  @Override
  public List<BookDtoWithStatus> findAllBooks(String title, String author)
  {
    List<Book> books = bookRepository.findAllBooks(title, author, "");

    List<BookDtoWithStatus> dtoWithStatusBook = new ArrayList<>();
    if (!books.isEmpty()) {
      for (Book b : books) {
        BookDtoWithStatus bookDto = new BookDtoWithStatus()
            .setId(b.getId())
            .setTitle(b.getTitle())
            .setGenre(b.getGenre())
            .setAuthor(b.getAuthor())
            .setRating(ratingRepository.getAveRating(b.getId()))  // added the rating for the book;
            .setEnabled(b.getEnabled())
            .setCountOfLibraries(libraryRepository.numberOfLibraries(b.getId())); // check if it works?
        dtoWithStatusBook.add(bookDto);
      }
    }
    return dtoWithStatusBook;
  }

  @Override
  public List<BookDtoNoUrl> findEnabledBooks(String title, String author)
  {
    List<Book> books = bookRepository.findAllBooks(title, author, "TRUE");
    List<BookDtoNoUrl> dtoBooks = new ArrayList<>();
    if (!books.isEmpty()) {
      for (Book b : books) {
        BookDtoNoUrl bookDto = new BookDtoNoUrl()
            .setId(b.getId())
            .setTitle(b.getTitle())
            .setGenre(b.getGenre())
            .setRating(ratingRepository.getAveRating(b.getId()))
            .setAuthor(b.getAuthor());
        dtoBooks.add(bookDto);
      }
    }
    return dtoBooks;
  }

  @Override
  public Optional<Book> findById(Long id)
  {
    return Optional.ofNullable(bookRepository.findById(id));
  }

  @Override
  public Optional<Book> findByTitle(String title)
  {
    return Optional.ofNullable(bookRepository.findByTitle(title));
  }

  @Override
  public void updateStatus(Long id, Boolean enable)
  {
    bookRepository.updateStatus(id, enable);
  }

}
