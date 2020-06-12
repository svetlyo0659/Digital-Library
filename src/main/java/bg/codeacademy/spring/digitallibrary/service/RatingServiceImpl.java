package bg.codeacademy.spring.digitallibrary.service;

import bg.codeacademy.spring.digitallibrary.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService
{
  private final RatingRepository ratingRepository;

  public RatingServiceImpl(RatingRepository ratingRepository)
  {
    this.ratingRepository = ratingRepository;
  }

  @Override
  public Double getAveRating(Long book_id)
  {
    return ratingRepository.getAveRating(book_id);
  }

  @Override
  public void rate(Long user_id, Long book_id, Double rating){
    ratingRepository.rate(user_id, book_id, rating);
  }

  @Override
  public Optional<Double> getYourRating(Long user_id, Long book_id)
  {
    return Optional.ofNullable(ratingRepository.getYourRating(user_id, book_id));
  }

  @Override  /// used for testing
  public void deleteRating(Long bookId)
  {
    ratingRepository.deleteBook(bookId);
  }
}
