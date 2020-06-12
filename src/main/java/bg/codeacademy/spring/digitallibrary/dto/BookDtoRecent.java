package bg.codeacademy.spring.digitallibrary.dto;

import java.sql.Date;
import java.time.LocalDateTime;

public class BookDtoRecent
{
  private Long          id;
  private String        title;
  private String        genre;
  private String        author;
  private Double rating;
  private LocalDateTime   date;

  public BookDtoRecent()
  {
  }

  public Long getId()
  {
    return id;
  }

  public BookDtoRecent setId(Long id)
  {
    this.id = id;
    return this;
  }

  public String getTitle()
  {
    return title;
  }

  public BookDtoRecent setTitle(String title)
  {
    this.title = title;
    return this;
  }

  public String getGenre()
  {
    return genre;
  }

  public BookDtoRecent setGenre(String genre)
  {
    this.genre = genre;
    return this;
  }

  public String getAuthor()
  {
    return author;
  }

  public BookDtoRecent setAuthor(String author)
  {
    this.author = author;
    return this;
  }

  public Double getRating()
  {
    return rating;
  }

  public BookDtoRecent setRating(Double rating)
  {
    this.rating = rating;
    return this;
  }

  public LocalDateTime getDate()
  {
    return date;
  }

  public BookDtoRecent setDate(LocalDateTime date)
  {
    this.date = date;
    return this;
  }
}
