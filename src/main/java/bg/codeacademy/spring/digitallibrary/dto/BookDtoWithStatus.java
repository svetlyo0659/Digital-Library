package bg.codeacademy.spring.digitallibrary.dto;

public class BookDtoWithStatus
{
  private Long   id;
  private String title;
  private String genre;
  private String author;
  private Double rating;
  private Boolean enabled;
  private Integer countOfLibraries;

  public BookDtoWithStatus()
  {
  }

  public Long getId()
  {
    return id;
  }

  public BookDtoWithStatus setId(Long id)
  {
    this.id = id;
    return this;
  }

  public String getTitle()
  {
    return title;
  }

  public BookDtoWithStatus setTitle(String title)
  {
    this.title = title;
    return this;
  }

  public String getGenre()
  {
    return genre;
  }

  public BookDtoWithStatus setGenre(String genre)
  {
    this.genre = genre;
    return this;
  }

  public String getAuthor()
  {
    return author;
  }

  public BookDtoWithStatus setAuthor(String author)
  {
    this.author = author;
    return this;
  }

  public Double getRating()
  {
    return rating;
  }

  public BookDtoWithStatus setRating(Double rating)
  {
    this.rating = rating;
    return this;
  }

  public Boolean getEnabled()
  {
    return enabled;
  }

  public BookDtoWithStatus setEnabled(Boolean enabled)
  {
    this.enabled = enabled;
    return this;
  }

  public Integer getCountOfLibraries()
  {
    return countOfLibraries;
  }

  public BookDtoWithStatus setCountOfLibraries(Integer countOfLibraries)
  {
    this.countOfLibraries = countOfLibraries;
    return this;
  }
}
