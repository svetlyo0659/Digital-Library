package bg.codeacademy.spring.digitallibrary.repository;


import bg.codeacademy.spring.digitallibrary.model.User;

import java.util.List;

public interface UserDao
{
  //works
  List<User> findAllUsers();

  int save(User user);

  Boolean containsUser(Long userId);

  int deleteUser(Long userId);

  User findUserById(Long userId);

  User findUserByUsername(String Username);

  void enableUser(Long userId, Boolean isEnabled);


}
