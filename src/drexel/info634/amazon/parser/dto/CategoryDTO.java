package drexel.info634.amazon.parser.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for a Category, which is essentially a hierarchy of individual
 * sub-categories. Example: |Books[283155]|Subjects[1000]|Religion &
 * Spirituality[22]|Christianity[12290]|Clergy[12360]|Preaching[12368]
 *
 * Created by Rob on 4/16/2015.
 */
public class CategoryDTO {

  private final List<CategoryItemDTO> categoryItems = new ArrayList<>();

  public void add(CategoryItemDTO item) {
    categoryItems.add(item);
  }

  public CategoryItemDTO[] getAll() {
    return (CategoryItemDTO[]) categoryItems.toArray();
  }

  public CategoryItemDTO get(int index) {
    return categoryItems.get(index);
  }

  @Override
  public String toString() {
    String result = "";
    for (CategoryItemDTO categoryItem : categoryItems) {
      result += categoryItem.toString();
    }
    return result;
  }
}
