package amazon_product_metadata_parser.output;

import java.io.IOException;

import amazon_product_metadata_parser.dto.ProductDTO;

/**
 * Simply outputs the Product data to a String. DO NOT USE ON THE ACTUAL DATA FILE IT IS TOO LARGE.
 * This is meant for using on smaller data files such as a sample data file.
 * <p>
 * Created by Rob Wise <robert.wise@outlook.com> on 4/16/2015.
 */
public class StringOutput implements Output {

  public String result = "";

  @Override
  public void open() throws IOException {
    // Nothing to do
  }

  @Override
  public void createProduct(ProductDTO productDTO) throws IOException {
    result += productDTO.toString() + String.format("%n");
  }

  @Override
  public void close() throws IOException {
    // Nothing to do
  }
}
