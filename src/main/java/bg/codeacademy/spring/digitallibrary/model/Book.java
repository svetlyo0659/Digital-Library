package bg.codeacademy.spring.digitallibrary.model;


public class Book
{

  private Long    id;
  private String  title;
  private String  genre;
  private String  author;
  private Boolean enabled;

  public Book()
  {
  }

  public Boolean getEnabled()
  {
    return enabled;
  }

  public Book setEnabled(Boolean enabled)
  {
    this.enabled = enabled;
    return this;
  }

  public String getGenre()
  {
    return genre;
  }

  public Book setGenre(String genre)
  {
    this.genre = genre;
    return this;
  }

  public Long getId()
  {
    return id;
  }

  public Book setId(Long id)
  {
    this.id = id;
    return this;
  }

  public String getAuthor()
  {
    return author;
  }

  public Book setAuthor(String author)
  {
    this.author = author;
    return this;
  }

  public String getTitle()
  {
    return title;
  }

  public Book setTitle(String title)
  {
    this.title = title;
    return this;
  }
}
