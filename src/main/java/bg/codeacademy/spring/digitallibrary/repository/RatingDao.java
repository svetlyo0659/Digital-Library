package bg.codeacademy.spring.digitallibrary.repository;

public interface RatingDao
{
  Double getAveRating(Long bookId);

  void rate(Long user_id, Long bookId, Double rating);

  Double getYourRating(Long userId, Long bookId);

  Boolean containsUser(Long userId);

  int deleteUser(Long userId);

  Boolean containsBook(Long bookId);

  int deleteBook(Long bookId);
}
