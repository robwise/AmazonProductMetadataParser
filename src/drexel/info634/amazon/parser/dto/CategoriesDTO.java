package drexel.info634.amazon.parser.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a collection of {@code CategoryItemDTO}s. Created by Rob on 4/16/2015.
 */
public class CategoriesDTO {

  private final List<CategoryDTO> categories = new ArrayList<>();
  private String count = "0";

  public void add(CategoryDTO category) {
    categories.add(category);
  }

  public CategoryDTO[] getAll() {
    return (CategoryDTO[]) categories.toArray();
  }

  public CategoryDTO get(int index) {
    return categories.get(index);
  }

  public String getCount() {
    return count;
  }

  public void setCount(String count) {
    this.count = count;
  }

  @Override
  public String toString() {
    String result = "";
    for (CategoryDTO category : categories) {
      result += "Category: " + category.toString();
    }
    return result;
  }
}
