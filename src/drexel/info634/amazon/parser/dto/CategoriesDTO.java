package drexel.info634.amazon.parser.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a collection of {@code CategoryItemDTO}s. Created by Rob on 4/16/2015.
 */
public class CategoriesDTO {

  private int count = 0;
  private final List<CategoryDTO> categories = new ArrayList<>();

  public void add(CategoryDTO category) {
    categories.add(category);
    count++;
  }

  public CategoryDTO[] getAll() {
    return (CategoryDTO[]) categories.toArray();
  }

  public CategoryDTO get(int index) {
    return categories.get(index);
  }

  public int getCount() {
    return count;
  }
}
