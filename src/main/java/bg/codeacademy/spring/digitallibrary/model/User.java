package bg.codeacademy.spring.digitallibrary.model;

public class User
{

  private Long    id;
  private String  username;
  private String  name;
  private String  password;
  private String  role;
  private Boolean isEnabled;


//  public User(int id,String username,String password,String role)
//  {
//    this.id=id;
//    this.username = username;
//    this.password = password;
//    this.role = role;
//  }

  public User(){
    //default c.
  }

  public Long getId()
  {
    return id;
  }

  public User setId(Long id)
  {
    this.id = id;
    return this;
  }

  public String getUsername()
  {
    return username;
  }

  public User setUsername(String username)
  {
    this.username = username;
    return this;
  }

  public String getPassword()
  {
    return password;
  }

  public User setPassword(String password)
  {
    this.password = password;
    return this;
  }

  public String getRole()
  {
    return role;
  }

  public User setRole(String role)
  {
    this.role = role;
    return this;
  }

  public Boolean getEnabled()
  {
    return isEnabled;
  }

  public User setEnabled(Boolean enabled)
  {
    isEnabled = enabled;
    return this;
  }


  public String getName()
  {
    return name;
  }

  public User setName(String name)
  {
    this.name = name;
    return this;
  }
}
