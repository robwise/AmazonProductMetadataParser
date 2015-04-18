package drexel.info634.amazon.parser.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a collection of review items. Example: '  reviews: total: 2  downloaded:
 * 2  avg rating: 5'
 *
 * Created by Rob on 4/15/2015.
 */
public class ReviewsDTO {

  private String total;
  private String downloaded;
  private String avgRating;
  private List<ReviewDTO> reviews = new ArrayList<>();

  public ReviewDTO[] getAll() {
    return (ReviewDTO[]) reviews.toArray();
  }

  public ReviewDTO get(int index) {
    return reviews.get(index);
  }

  public void add(ReviewDTO review) {
    reviews.add(review);
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getDownloaded() {
    return downloaded;
  }

  public void setDownloaded(String downloaded) {
    this.downloaded = downloaded;
  }

  public String getAvgRating() {
    return avgRating;
  }

  public void setAvgRating(String avgRating) {
    this.avgRating = avgRating;
  }

  @Override
  public String toString() {
    String result = "Reviews:" + System.lineSeparator();
    for (ReviewDTO review : reviews) {
      result += System.lineSeparator() + review.toString();
    }
    return result;
  }
}
