package amazon_product_metadata_parser.dto;

/**
 * Data Transfer Object for a category item (e.g., "Books[283155]").
 */
public class CategoryItemDTO {

  public final String  name;
  public final String  id;
  public final String  parentCategoryItem;
  public final boolean highestParentFlag;
  public final boolean lowestChildFlag;
  public final int     depthFromParent;

  CategoryItemDTO(String name, String id, String parentCategoryItem,
                  boolean highestParentFlag, boolean lowestChildFlag, int depthFromParent)
      throws FailedValidationException {
    this.name = name;
    this.id = id;
    this.parentCategoryItem = parentCategoryItem;
    this.highestParentFlag = highestParentFlag;
    this.lowestChildFlag = lowestChildFlag;
    this.depthFromParent = depthFromParent;
    throwExceptionIfInvalid();
  }

  private void throwExceptionIfInvalid() throws FailedValidationException {
    if (parentCategoryItem.equals(id) && !highestParentFlag) {
      throw new FailedValidationException("Parent Category not equal");
    }
    if (highestParentFlag && !parentCategoryItem.equals(id)) {
      throw new FailedValidationException("Highest Parent Flag set to true but has "
                                          + "non-self-referencing parent category");
    }
    if (null == id) {
      throw new FailedValidationException("ID is null");
    }
    if (null == name) {
      throw new FailedValidationException("Name is null");
    }
    if (depthFromParent > 0 && highestParentFlag) {
      throw new FailedValidationException("Flagged as highest parent but has non-zero depth from "
                                          + "parent");
    }
    if (depthFromParent == 0 && !highestParentFlag) {
      throw new FailedValidationException("Zero depth from parent but not flagged as highest "
                                          + "parent");
    }
  }

  @Override
  public String toString() {
    return String.format("  Category Name: %s%n"
                         + "    Category ID: %s%n"
                         + "    Parent Category: %s%n"
                         + "    Is Highest Parent: %b%n"
                         + "    Is Lowest Child: %b%n"
                         + "    Depth from Parent: %d%n",
                         name,
                         id,
                         parentCategoryItem,
                         highestParentFlag,
                         lowestChildFlag,
                         depthFromParent);
  }
}
