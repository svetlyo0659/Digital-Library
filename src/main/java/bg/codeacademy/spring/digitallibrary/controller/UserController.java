package bg.codeacademy.spring.digitallibrary.controller;

import bg.codeacademy.spring.digitallibrary.dto.UserDto;
import bg.codeacademy.spring.digitallibrary.model.User;
import bg.codeacademy.spring.digitallibrary.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController
{

  private final UserServiceImpl userService;


  @Autowired
  private UserController(UserServiceImpl userService)
  {
    this.userService = userService;

  }

  //WORKS
  @PostMapping()
  public ResponseEntity<?> createUser(@RequestParam(value = "username") String username,
                                      @RequestParam(value = "password") String password,
                                      @RequestParam(value = "name") String name,
                                      @RequestParam(value = "role", required = false, defaultValue = "READER") String role,
                                      @RequestParam(value = "isEnabled", required = false, defaultValue = "true")
                                          Boolean isEnabled, Principal p)
  {
    Optional<User> user = userService.findUserByUsername(username);
    if (!user.isPresent()) {
      userService.save(new User()
          .setUsername(username)
          .setName(name)
          .setPassword(new BCryptPasswordEncoder().encode(password))
          .setRole(role)
          .setEnabled(isEnabled));

      return ResponseEntity.ok("User Created");
    }
    return ResponseEntity.badRequest().body("Username taken");
  }

  /**
   * @param userId
   * @param p
   * @return Deletes the user from the DB
   */

  @DeleteMapping("/{userId}/remove")
  public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") Long userId,
                                      Principal p)
  {
    Optional<User> user = userService.findUserById(userId);
    if (user.isPresent()) {

      userService.delete(userId);
      return ResponseEntity.ok("User deleted");
    }
    return ResponseEntity.badRequest().body("User not found"); // change to user not found
  }


  /**
   * @param isEnabled
   * @return set the user to active/inactive
   * WORKS
   */

  @PatchMapping("/{userId}/enable")
  public ResponseEntity<?> enableUser(@PathVariable(value = "userId") Long userId,
                                      @RequestParam(value = "enable") Boolean isEnabled)
  {

    Optional<User> user = userService.findUserById(userId);
    if (user.isPresent()) {
      userService.enableUser(userId, isEnabled);
      if (isEnabled.equals(true)) {
        return ResponseEntity.ok("User is active");
      }
      else if (isEnabled.equals(false)) {
        return ResponseEntity.ok("User is inactive");
      }
    }
    return ResponseEntity.badRequest().body("User not found!");
  }


  /**
   * @return all DTO USERS
   */

  @GetMapping
  public ResponseEntity<?> getUsers(Principal p)
  {
    Optional<User> currentUser = userService.findUserByUsername(p.getName());
    if (currentUser.isPresent() && currentUser.get().getEnabled() == true) {
      List<UserDto> allUsers = userService.findAllUsers();
      if (!allUsers.isEmpty()) {
        return ResponseEntity.ok(allUsers);
      }
      return ResponseEntity.badRequest().body("No users found!");
    }
    return ResponseEntity.badRequest().body("Your account is temporary disabled!");
  }
}
