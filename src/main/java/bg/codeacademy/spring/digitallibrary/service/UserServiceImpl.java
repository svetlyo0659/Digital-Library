package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.dto.UserDto;
import bg.codeacademy.spring.digitallibrary.model.User;
import bg.codeacademy.spring.digitallibrary.repository.LibraryRepository;
import bg.codeacademy.spring.digitallibrary.repository.RatingRepository;
import bg.codeacademy.spring.digitallibrary.repository.RecentRepository;
import bg.codeacademy.spring.digitallibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService
{
  private final UserRepository    userRepository;
  private final LibraryRepository libraryRepository;
  private final RatingRepository  ratingRepository;
  private final RecentRepository  recentRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, LibraryRepository libraryRepository, RatingRepository ratingRepository, RecentRepository recentRepository)
  {
    this.userRepository = userRepository;
    this.libraryRepository = libraryRepository;
    this.ratingRepository = ratingRepository;
    this.recentRepository = recentRepository;
  }

  @Override
  public List<UserDto> findAllUsers()
  {
    List<User> users = userRepository.findAllUsers();

    List<UserDto> dtoWithStatusUsers = new ArrayList<>();
    if (!users.isEmpty()) {
      for (User u : users) {
        UserDto userDto = new UserDto()
            .setUserId(u.getId())
            .setUsername(u.getUsername())
            .setName(u.getName())
            .setRole(u.getRole())
            .setEnabled(u.getEnabled());
        dtoWithStatusUsers.add(userDto);
      }
    }
    return dtoWithStatusUsers;
  }

  @Override
  public void save(User user)
  {
    userRepository.save(user);
  }

  @Override
  public void delete(Long userId)
  {
    if (libraryRepository.containsUser(userId)) {
      libraryRepository.deleteUser(userId);
    }
    if (ratingRepository.containsUser(userId)) {
      ratingRepository.deleteUser(userId);
    }
    if (recentRepository.containsUser(userId)) {
      recentRepository.deleteUser(userId);
    }
    if (userRepository.containsUser(userId)) {
      userRepository.deleteUser(userId);
    }
  }

  @Override
  public Optional<User> findUserById(Long userId)
  {
    return Optional.ofNullable(userRepository.findUserById(userId));
  }

  @Override
  public Optional<User> findUserByUsername(String username)
  {
    return Optional.ofNullable(userRepository.findUserByUsername(username));
  }

  @Override
  public void enableUser(Long userId, Boolean isEnabled)
  {
    userRepository.enableUser(userId, isEnabled);
  }


  /// used for test only
  public void deleteByUsername(String username)
  {
    userRepository.deleteByUsername(username);
  }

}
