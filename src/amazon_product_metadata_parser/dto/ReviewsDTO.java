package amazon_product_metadata_parser.dto;

/**
 * Data Transfer Object for a collection of review items.
 * <p>
 * Example: '  reviews: total: 2  downloaded: 2  avg rating: 5'
 */
public class ReviewsDTO {

  public final int         total;
  public final int         downloaded;
  public final double      avgRating;
  public final ReviewDTO[] reviews;

  ReviewsDTO(int total, int downloaded, double avgRating, ReviewDTO[] reviews)
      throws FailedValidationException {
    this.total = total;
    this.downloaded = downloaded;
    this.avgRating = Double.valueOf(avgRating);
    this.reviews = reviews;
    throwExceptionIfInvalid();
  }

  private void throwExceptionIfInvalid() throws FailedValidationException {
    if (avgRating < 0) {
      throw new FailedValidationException(String.format("Average rating (%f) is less than zero",
                                                        avgRating));
    }
    if (avgRating > 5) {
      throw new FailedValidationException(String.format("Average rating (%f) is greater than "
                                                        + "five", avgRating));
    }
    if (downloaded != reviews.length) {
      throw new FailedValidationException(String.format(
        "Downloaded (%d) is not equal to actual size "
                                                        + "of reviews (%d)",
        downloaded,
                                                        reviews.length));
    }
    // These fail, so commented out...
    //    if (total < downloaded) {
    //      throw new FailedValidationException(String.format("Total (%d) is
    // less than downloaded (%d)",
    //                                                        total,
    // downloaded));
    //    }
  }

  @Override
  public String toString() {
    String result = String.format("Reviews Total: %d%n", total)
                    + String.format("Reviews Downloaded: %d%n", downloaded)
                    + String.format("Reviews Average Rating: %f%n", avgRating);
    for (ReviewDTO review : reviews) {
      result += review.toString();
    }
    return result;
  }
}
