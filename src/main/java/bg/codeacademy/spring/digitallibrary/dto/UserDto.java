package bg.codeacademy.spring.digitallibrary.dto;

public class UserDto
{
  private Long    userId;
  private String  username;
  private String  name;
  private String  role;
  private Boolean enabled;


  public UserDto()
  {
    // default constructor
  }


  public String getUsername()
  {
    return username;
  }

  public UserDto setUsername(String username)
  {
    this.username = username;
    return this;
  }

  public String getRole()
  {
    return role;
  }

  public UserDto setRole(String role)
  {
    this.role = role;
    return this;
  }


  public Long getUserId()
  {
    return userId;
  }

  public UserDto setUserId(Long userId)
  {
    this.userId = userId;
    return this;
  }

  public Boolean getEnabled()
  {
    return enabled;
  }

  public UserDto setEnabled(Boolean enabled)
  {
    this.enabled = enabled;
    return this;
  }

  public String getName()
  {
    return name;
  }

  public UserDto setName(String name)
  {
    this.name = name;
    return this;
  }
}
