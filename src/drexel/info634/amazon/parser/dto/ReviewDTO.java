package drexel.info634.amazon.parser.dto;

/**
 * Data Transfer Object for a review. Example: '    2000-7-28  cutomer: A2JW67OY8U6HHK  rating: 5
 * votes:  10  helpful:   9' Created by Rob on 4/16/2015.
 */
public class ReviewDTO {

  private String date;
  private String customer;
  private String rating;
  private String votes;
  private String helpful;

  public String getHelpful() {
    return helpful;
  }

  public void setHelpful(String helpful) {
    this.helpful = helpful;
  }

  public String getVotes() {
    return votes;
  }

  public void setVotes(String votes) {
    this.votes = votes;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
