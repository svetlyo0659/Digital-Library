package bg.codeacademy.spring.digitallibrary.config;

import bg.codeacademy.spring.digitallibrary.exception.UnauthorizedUserException;
import bg.codeacademy.spring.digitallibrary.model.User;
import bg.codeacademy.spring.digitallibrary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService
{

  private final UserService userService;

  @Autowired
  public UserDetailsServiceImpl(UserService userService)
  {
    this.userService = userService;
  }

  public UserDetails loadUserByUsername(String username) throws
      UsernameNotFoundException, UnauthorizedUserException
  {

    Optional<User> admin = userService.findUserByUsername("admin");
    if (!admin.isPresent()) {
      userService.save(new User()
          .setUsername("admin")
          .setName("admin")
          .setRole("ADMIN")
          .setEnabled(true)
          .setPassword(new BCryptPasswordEncoder().encode("123456")));
    }
    Optional<User> optionalUser = userService.findUserByUsername(username);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();

      if (user.getUsername().equalsIgnoreCase(username)) {
        if (user.getEnabled().equals(false)) {
          throw new UnauthorizedUserException("USER UNAUTHORIZED!");
        }

        org.springframework.security.core.userdetails.User.UserBuilder builder =
            org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .roles(user.getRole())
                .password(user.getPassword());
        return builder.build();
      }
      throw new UsernameNotFoundException("Username not found!");
    }
    throw new UsernameNotFoundException("Username not found!");
  }

}
