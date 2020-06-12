package bg.codeacademy.spring.digitallibrary.dto;

public class BookDtoNoUrl
{
  private Long   id;
  private String title;
  private String genre;
  private String author;
  private Double rating;

  public Long getId()
  {
    return id;
  }

  public BookDtoNoUrl setId(Long id)
  {
    this.id = id;
    return this;
  }

  public String getTitle()
  {
    return title;
  }

  public BookDtoNoUrl setTitle(String title)
  {
    this.title = title;
    return this;
  }

  public String getGenre()
  {
    return genre;
  }

  public BookDtoNoUrl setGenre(String genre)
  {
    this.genre = genre;
    return this;
  }

  public String getAuthor()
  {
    return author;
  }

  public BookDtoNoUrl setAuthor(String author)
  {
    this.author = author;
    return this;
  }

  public Double getRating()
  {
    return rating;
  }

  public BookDtoNoUrl setRating(Double rating)
  {
    this.rating = rating;
    return this;
  }
}
