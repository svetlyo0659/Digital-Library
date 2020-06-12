package bg.codeacademy.spring.digitallibrary.dto;

public class BookDto
{
  private Long   id;
  private String title;
  private String genre;
  private String author;
  private Double rating;
  private String url;

  public BookDto()
  {
  }

  public Long getId()
  {
    return id;
  }

  public BookDto setId(Long id)
  {
    this.id = id;
    return this;
  }

  public String getTitle()
  {
    return title;
  }

  public BookDto setTitle(String title)
  {
    this.title = title;
    return this;
  }

  public String getGenre()
  {
    return genre;
  }

  public BookDto setGenre(String genre)
  {
    this.genre = genre;
    return this;
  }

  public String getAuthor()
  {
    return author;
  }

  public BookDto setAuthor(String author)
  {
    this.author = author;
    return this;
  }

  public Double getRating()
  {
    return rating;
  }

  public BookDto setRating(Double rating)
  {
    this.rating = rating;
    return this;
  }

  public String getUrl()
  {
    return url;
  }

  public BookDto setUrl(String url)
  {
    this.url = url;
    return this;
  }

}
