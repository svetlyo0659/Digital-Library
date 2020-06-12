package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.dto.UserDto;
import bg.codeacademy.spring.digitallibrary.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService
{

  List<UserDto> findAllUsers();

  void save(User user);

  void delete(Long userId);

  Optional<User> findUserById(Long id);

  Optional<User> findUserByUsername(String Username);

  void enableUser(Long userId, Boolean isEnabled);

}
