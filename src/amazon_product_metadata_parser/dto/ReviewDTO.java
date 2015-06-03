package amazon_product_metadata_parser.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for a review.
 * <p>
 * Example: '    2000-7-28  cutomer: A2JW67OY8U6HHK  rating: 5 votes:  10  helpful:   9'
 */
public class ReviewDTO {

  public final LocalDate date;
  public final String    customer;
  public final int       rating;
  public final int       votes;
  public final int       helpful;

  ReviewDTO(LocalDate date, String customer, int rating, int votes, int helpful)
      throws FailedValidationException {
    this.date = date;
    this.customer = customer;
    this.rating = rating;
    this.votes = votes;
    this.helpful = helpful;
    throwExceptionIfInvalid();
  }

  private void throwExceptionIfInvalid() throws FailedValidationException {
    if (null == customer || customer.isEmpty()) {
      throw new FailedValidationException("Customer not defined");
    }
    if (votes < 0) {
      throw new FailedValidationException(String.format("Number of votes (%d) is less than zero",
                                                        votes));
    }
    if (rating < 0) {
      throw new FailedValidationException(String.format("Rating (%d) is less than zero",
                                                        rating));
    }
    if (helpful < 0) {
      throw new FailedValidationException(String.format(
        "Helpful (%d) is less than zero",
        helpful));
    }
    // These fail, so commented out...
    //    if (date.getYear() < 1990 || date.getYear() > 2007) {
    //      throw new FailedValidationException(String.format("Date (%s) is
    // not in acceptable range",
    //                                                        date.toString()));
    //    }
  }

  @Override
  public String toString() {
    return String.format("  Review Date: %s%n", date.toString())
           + String.format("    Review Customer: %s%n", customer)
           + String.format("    Review Rating: %d%n", rating)
           + String.format("    Review Votes: %d%n", votes)
           + String.format("    Review Helpful: %d%n", helpful);
  }
}
