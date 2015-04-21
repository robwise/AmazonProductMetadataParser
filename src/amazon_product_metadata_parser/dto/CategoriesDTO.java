package amazon_product_metadata_parser.dto;

/**
 * Data Transfer Object for a collection of {@code CategoryItemDTO}s.
 */
public class CategoriesDTO {

  public final CategoryDTO[] categories;
  public final int           count;

  CategoriesDTO(CategoryDTO[] categories, int count) throws FailedValidationException {
    this.categories = categories;
    this.count = count;
    throwExceptionIfInvalid();
  }

  private void throwExceptionIfInvalid() throws FailedValidationException {
    if (count != categories.length) {
      String errorMsg = "Category total given is " + count
                        + " but there are " + categories.length + " category items";
      throw new FailedValidationException(errorMsg);
    }
  }

  @Override
  public String toString() {
    String result = String.format("Categories: %d%n", count);
    for (CategoryDTO category : categories) {
      result += category.toString();
    }
    return result;
  }
}
