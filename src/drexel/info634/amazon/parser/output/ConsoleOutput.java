package drexel.info634.amazon.parser.output;

import java.io.IOException;

import drexel.info634.amazon.parser.dto.ProductDTO;

/**
 * Simply prints product data to the console.<br><br> Created by Rob Wise <robert.wise@outlook.com>
 * on 4/16/2015.
 */
public class ConsoleOutput implements Output {

  @Override
  public void open() throws IOException {
    return;
  }

  @Override
  public void createProduct(ProductDTO productDTO) throws IOException {
    System.out.println("ID: " + productDTO.id);
    System.out.println("ASIN: " + productDTO.asin);
    System.out.println("Title: " + productDTO.title);
    System.out.println("Group: " + productDTO.group);
    System.out.println();
  }

  @Override
  public void close() throws IOException {
    return;
  }
}
