package amazon_product_metadata_parser.dto;

/**
 * Data Transfer Object for a product. Use the {@code ProductFactory} class to build from an array
 * of raw text data in the format used by the dataset.
 */
@SuppressWarnings("SpellCheckingInspection")
public class ProductDTO {

  public final String          id;
  public final String          asin;
  public final String          title;
  public final String          group;
  public final int             salesrank;
  public final SimilarItemsDTO similarItems;
  public final CategoriesDTO   categories;
  public final ReviewsDTO      reviews;
  public final boolean         discontinued;

  public ProductDTO(String id, String asin, String title, String group, int salesrank,
                    SimilarItemsDTO similarItems, CategoriesDTO categories, ReviewsDTO reviews,
                    boolean discontinued) throws FailedValidationException {
    this.id = id;
    this.asin = asin;
    this.title = title;
    this.group = group;
    this.salesrank = salesrank;
    this.similarItems = similarItems;
    this.categories = categories;
    this.reviews = reviews;
    this.discontinued = discontinued;
    throwExceptionIfInvalid();
  }

  // Discontinued products do not have any data besides id and asin
  private void throwExceptionIfInvalid() throws FailedValidationException {

    boolean emptyId = id.isEmpty();
    boolean emptyASIN = asin.isEmpty();
    boolean nullTitle = (null == title);
    boolean nullGroup = (null == group);
    boolean negativeSalesrank = salesrank < 0;
    boolean nullSimilarItems = (null == similarItems);
    boolean nullCategories = (null == categories);
    boolean nullReviews = (null == reviews);
    boolean nullNonDiscontinuedValues = nullTitle && nullGroup && negativeSalesrank
                                        && nullSimilarItems
                                        && nullCategories && nullReviews;

    // Validations for all products
    if (emptyId) {
      throw new FailedValidationException("Id is not defined");
    }
    if (emptyASIN) {
      throw new FailedValidationException("ASIN is not defined");
    }

    // Validations for discontinued products
    if (discontinued && !(nullNonDiscontinuedValues)) {
      throw new FailedValidationException("Discontinued is true but has non-discontinued data");
    }

    // Validations for non-discontinued products
    if (!discontinued) {
      if (nullNonDiscontinuedValues) {
        throw new FailedValidationException("Discontinued is false but has no non-discontinued "
                                            + "data");
      }
      if (negativeSalesrank) {
        throw new FailedValidationException(String.format(
            "Salesrank (%d) is negative and product is not a discontinued product",
            salesrank));
      }
    }
  }

  public String toString() {
    String retString = String.format("ID: %s%n", id) + String.format("ASIN: %s%n", asin);
    if (discontinued) {
      retString += String.format("Discontinued Product%n");
    } else {
      retString += String.format("Title: %s%n", title)
                   + String.format("Group: %s%n", group)
                   + String.format("Sales Rank: %d%n", salesrank)
                   + similarItems.toString()
                   + categories.toString()
                   + reviews.toString();
    }
    return retString;
  }
}
