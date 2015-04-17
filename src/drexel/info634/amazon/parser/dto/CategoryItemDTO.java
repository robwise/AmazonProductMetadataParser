package drexel.info634.amazon.parser.dto;

/**
 * Data Transfer Object for a category item (e.g., "Books[283155]"). Created by Rob on 4/15/2015.
 */
public class CategoryItemDTO {

  private String name;
  private String id;

  public CategoryItemDTO(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }
}
