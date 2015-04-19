package amazon_product_metadata_parser.dto;

/**
 * Data Transfer Object for a Category, which is essentially a hierarchy of individual
 * sub-categories.
 * <p>
 * Example: |Books[283155]|Subjects[1000]|Religion &
 * Spirituality[22]|Christianity[12290]|Clergy[12360]|Preaching[12368]
 */
public class CategoryDTO {

  public final CategoryItemDTO[] categoryItems;
  public final int               categoryItemCount;

  public CategoryDTO(CategoryItemDTO[] categoryItems, int categoryItemCount)
      throws FailedValidationException {
    this.categoryItems = categoryItems;
    this.categoryItemCount = categoryItemCount;
    throwExceptionIfInvalid();
  }

  private void throwExceptionIfInvalid() throws FailedValidationException {
    if (categoryItemCount != categoryItems.length) {
      String errorMsg = "Category total given is " + categoryItemCount
                        + " but there are " + categoryItems.length + " category items";
      throw new FailedValidationException(errorMsg);
    }
  }

  @Override
  public String toString() {
    String retString = String.format("Category Items: %d%n", categoryItemCount);
    for (CategoryItemDTO categoryItem : categoryItems) {
      retString += categoryItem.toString();
    }
    return retString;
  }
}
