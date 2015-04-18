package drexel.info634.amazon.parser.output;

import java.io.IOException;

import drexel.info634.amazon.parser.dto.ProductDTO;

/**
 * Simply outputs the Product data to a String. DO NOT USE ON ACTUAL DATA FILE IT IS TOO LARGE. This
 * is meant for using on smaller data files such as a sample data file. <br><br>Created by Rob Wise
 * <robert.wise@outlook.com> on 4/16/2015.
 */
public class StringOutput implements Output {

  public String result = "";

  @Override
  public void open() throws IOException {
    return;
  }

  @Override
  public void createProduct(ProductDTO productDTO) throws IOException {
    result += productDTO.id + System.lineSeparator();
    if (productDTO.discontinued.equals("DISCONTINUED")) {
      result += productDTO.discontinued + System.lineSeparator();
      return;
    }
    result += productDTO.asin + System.lineSeparator();
    result += productDTO.title + System.lineSeparator();
    result += productDTO.group + System.lineSeparator();
    result += productDTO.salesrank + System.lineSeparator();
    String similarCount = productDTO.similarCount + System.lineSeparator();
    result += similarCount;
    if (similarCount != null && !productDTO.similarCount.equals("0")) {
      for (String similarItem : productDTO.similarItems) {
        result += similarItem + System.lineSeparator();
      }
    }
    if (productDTO.categories != null) {
      result += productDTO.categories.toString() + System.lineSeparator();
    }
    if (productDTO.reviews != null) {
      result += productDTO.reviews.toString() + System.lineSeparator();
    }
  }

  @Override
  public void close() throws IOException {
    return;
  }
}
