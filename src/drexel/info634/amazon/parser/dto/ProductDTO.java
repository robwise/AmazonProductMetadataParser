package drexel.info634.amazon.parser.dto;

/**
 * Data Transfer Object for a product. Use the {@code ProductFactory} class to build from an array
 * of raw text data in the format used by the dataset.
 *
 * Created by Rob on 4/15/2015.
 */
public class ProductDTO {

  public String id;
  public String asin;
  public String title;
  public String group;
  public String salesrank;
  public String similarCount;
  public String[] similarItems;
  public String categoriesCount;
  public CategoriesDTO categories;
  public ReviewsDTO reviews;
  public String discontinued;
}
