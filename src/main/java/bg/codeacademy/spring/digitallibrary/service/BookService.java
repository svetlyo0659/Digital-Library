package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.dto.BookDtoNoUrl;
import bg.codeacademy.spring.digitallibrary.dto.BookDtoWithStatus;
import bg.codeacademy.spring.digitallibrary.model.Book;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

public interface BookService
{
  String uploadFile(MultipartFile file) throws IOException;

  BookDtoNoUrl saveBook(String title, String genre, String author);

  Resource download(String fileName) throws MalformedURLException;

  void deleteBook(Long id) throws IOException;

  List<BookDtoWithStatus> findAllBooks(String title, String author);

  List<BookDtoNoUrl> findEnabledBooks(String title, String author);

  Optional<Book> findById(Long id);

  Optional<Book> findByTitle(String title);

  void updateStatus(Long id, Boolean enable);
}
