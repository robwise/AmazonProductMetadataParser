package amazon_product_metadata_parser.dto;

/**
 * Data Transfer Object for a collection of similar items.
 * <p>
 * Raw Data Example: '    2000-7-28  cutomer: A2JW67OY8U6HHK  rating: 5 votes:  10  helpful:   9'
 */
public class SimilarItemsDTO {

  public final String[] similarItemASINs;
  public final int      count;

  SimilarItemsDTO(String[] similarItemASINs, int count) {
    this.similarItemASINs = similarItemASINs;
    this.count = count;
  }

  @Override
  public String toString() {
    String retString = String.format("Similar Items:%n");
    for (String similarItemASIN : similarItemASINs) {
      retString += String.format("  %s%n", similarItemASIN);
    }
    return retString;
  }

  private void throwExceptionIfInvalid() throws FailedValidationException {
    if (count != similarItemASINs.length) {
      throw new FailedValidationException(String.format("Count of %d not equal to array size of %d",
                                                        count,
                                                        similarItemASINs.length));
    }
    for (int i = 0; i < similarItemASINs.length; i++) {
      if (null == similarItemASINs[i] || similarItemASINs[i].isEmpty()) {
        throw new FailedValidationException(String.format("Similar item #%d is not defined", i));
      }
    }
  }
}
