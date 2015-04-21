package amazon_product_metadata_parser.output;

import java.io.IOException;

import amazon_product_metadata_parser.dto.ProductDTO;

/**
 * Simply prints product data to the console.
 * <p>
 * Created by Rob Wise <robert.wise@outlook.com> on 4/16/2015.
 */
public class ConsoleOutput implements Output {

  @Override
  public void open() throws IOException {
    // Nothing to do
  }

  @Override
  public void execute(ProductDTO productDTO) throws IOException {
    System.out.println(productDTO.toString());
    System.out.println();
  }

  @Override
  public void close() throws IOException {
    // Nothing to do
  }
}
